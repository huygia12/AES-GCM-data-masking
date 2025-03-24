package encryption.algorithm.demos.aesgcmmasking.encryption;

public class InvalidTagException extends Exception {

    public InvalidTagException() { }

    public InvalidTagException(String message)
    {
        super(message);
    }
}
