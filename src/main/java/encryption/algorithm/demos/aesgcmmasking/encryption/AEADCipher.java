package encryption.algorithm.demos.aesgcmmasking.encryption;

public interface AEADCipher {
    void init(AEADParams params);

    void updateAAD(byte[] data);

    void processBlock(byte[] data);

    void computeMAC(byte[] out);

    String encrypt(String plaintext) throws WrongModeException;

    String decrypt(String ciphertext) throws WrongModeException;

    void verify(byte[] tag) throws InvalidTagException;
}

