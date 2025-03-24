package encryption.algorithm.demos.aesgcmmasking.encryption;

public class WrongModeException extends Exception {

    public WrongModeException() { }

    public WrongModeException(String message)
    {
        super(message);
    }
}
