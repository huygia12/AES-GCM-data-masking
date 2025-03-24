package encryption.algorithm.demos.aesgcmmasking.encryption;

import encryption.algorithm.demos.aesgcmmasking.encryption.aes.AES128Encryptor;
import encryption.algorithm.demos.aesgcmmasking.encryption.aes.AES128EncryptorImpl;
import encryption.algorithm.demos.aesgcmmasking.encryption.galois.GF128Multiplier;
import encryption.algorithm.demos.aesgcmmasking.encryption.galois.GF128MultiplierImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AESGCM implements AEADCipher {
    private final GF128Multiplier GF;
    private final AES128Encryptor encryptor;
    private byte[] tag;
    private byte[] counter;
    private byte[] additionalMask;
    private int ciphertextLength;
    private int AADLength;
    CipherMode mode;

    public AESGCM() {
        GF = new GF128MultiplierImpl();
        encryptor = new AES128EncryptorImpl();
    }

    @Override
    public void init(AEADParams params) {
        byte[] key = params.getKey();
        byte[] iv = params.getIv();
        mode = params.getMode();

        AADLength = 0;
        ciphertextLength = 0;

        counter = new byte[16];
        System.arraycopy(iv, 0, counter, 0, iv.length);

        incrementCounter();

        encryptor.init(key);

        additionalMask = new byte[16];
        encryptor.encryptBlock(counter, additionalMask);

        byte[] H = new byte[16];
        encryptor.encryptBlock(H, H);
        GF.init(H);

        tag = new byte[16];
    }

    @Override
    public void updateAAD(byte[] data) {
        xorWithTagAndMultiplyByH(data);
        AADLength += (data.length * 8);
    }

    @Override
    public void processBlock(byte[] data) {
        if (mode == CipherMode.ENCRYPT) {
            incrementCounter();
            encryptCounterAndXORWithText(data);
            ciphertextLength += data.length * 8;
            xorWithTagAndMultiplyByH(data);

        } else if (mode == CipherMode.DECRYPT) {
            xorWithTagAndMultiplyByH(data);
            incrementCounter();
            encryptCounterAndXORWithText(data);
            ciphertextLength += data.length * 8;
        }
    }

    @Override
    public void computeMAC(byte[] out) {
        ByteBuffer buffer1 = ByteBuffer.allocate(Long.BYTES);
        buffer1.putLong(AADLength);
        byte[] byteArray1 = buffer1.array();

        ByteBuffer buffer2 = ByteBuffer.allocate(Long.BYTES);
        buffer2.putLong(ciphertextLength);
        byte[] byteArray2 = buffer2.array();

        byte[] concatenatedArray = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, concatenatedArray, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, concatenatedArray, byteArray1.length, byteArray2.length);

        xorWithTagAndMultiplyByH(concatenatedArray);

        byte[] T = additionalMask;
        for (int i = 0; i < tag.length; i++) {
            T[i] ^= tag[i];
        }

        System.arraycopy(T, 0, out, 0, T.length);
    }

    @Override
    public void verify(byte[] tag) throws InvalidTagException {

        byte[] holdArray = new byte[16];
        computeMAC(holdArray);

        if (!Arrays.equals(holdArray, tag)) {
            throw new InvalidTagException("Tags do not match!");
        }
    }

    @Override
    public String encrypt(String plaintext) throws WrongModeException {
        if(this.mode != CipherMode.ENCRYPT) throw new WrongModeException();

        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        StringBuilder cipherTextBuilder = new StringBuilder();

        for(int i = 0; i < plaintextBytes.length; i += 16) {
            byte[] block = Arrays.copyOfRange(plaintextBytes, i, Math.min(i + 16, plaintextBytes.length));
            processBlock(block);
            cipherTextBuilder.append(HexUtils.bytesToHex(block));
        }

        return cipherTextBuilder.toString();
    }

    @Override
    public String decrypt(String ciphertext) throws WrongModeException {
        if(this.mode != CipherMode.DECRYPT) throw new WrongModeException();

        byte[] cipherBytes = HexUtils.hexToBytes(ciphertext);
        ByteArrayOutputStream ouputStream = new ByteArrayOutputStream();

        for(int i = 0; i < cipherBytes.length; i += 16) {
            byte[] block = Arrays.copyOfRange(cipherBytes, i, Math.min(i + 16, cipherBytes.length));
            processBlock(block);
            try{
                ouputStream.write(block);
            } catch(IOException e){
               e.printStackTrace();
            }
        }

        return new String(ouputStream.toByteArray(), StandardCharsets.UTF_8);
    }

    private void incrementCounter() {
        int value = ByteBuffer.wrap(counter, 12, 4).getInt();
        value++;
        ByteBuffer.wrap(counter, 12, 4).putInt(value);
    }

    private void xorWithTagAndMultiplyByH(byte[] data) {
        int length = Math.min(data.length, tag.length);
        for (int i = 0; i < tag.length; i++) {
            if (i < length) {
                tag[i] ^= data[i];
            } else {
                tag[i] ^= 0;
            }
        }

        GF.multiplyByH(tag);
    }

    private void encryptCounterAndXORWithText(byte[] data) {
        byte[] temp = new byte[16];
        encryptor.encryptBlock(counter, temp);

        for (int i = 0; i < data.length; i++) {
            data[i] ^= temp[i];
        }
    }
}
