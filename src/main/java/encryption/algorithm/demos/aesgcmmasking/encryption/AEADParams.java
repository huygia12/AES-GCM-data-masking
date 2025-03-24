package encryption.algorithm.demos.aesgcmmasking.encryption;

public class AEADParams {
    private final byte[] key;
    private final byte[] iv;
    private final CipherMode mode;

    public AEADParams(byte[] key, byte[] iv, CipherMode mode) {
        this.key = key;
        this.iv = iv;
        this.mode = mode;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getIv() {
        return iv;
    }

    public CipherMode getMode() {
        return mode;
    }
}
