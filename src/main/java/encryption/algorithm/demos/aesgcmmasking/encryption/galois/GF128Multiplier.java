package encryption.algorithm.demos.aesgcmmasking.encryption.galois;

public interface GF128Multiplier {
    void init(byte[] H);
    void multiplyByH(byte[] X);
    void multiply(byte[] X, byte[] Y);
    byte[] getH();
}
