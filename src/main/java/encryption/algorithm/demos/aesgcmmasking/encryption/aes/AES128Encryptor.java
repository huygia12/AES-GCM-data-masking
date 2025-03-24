package encryption.algorithm.demos.aesgcmmasking.encryption.aes;

public interface AES128Encryptor {
    void init(byte[] key);
    void encryptBlock(byte[] input, byte[] output);
}
