package vnavesnoj.stomp_status_ping.exception;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class UsernameFieldNotFoundException extends RuntimeException {

    public UsernameFieldNotFoundException() {
        super();
    }

    public UsernameFieldNotFoundException(String message) {
        super(message);
    }

    public UsernameFieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameFieldNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UsernameFieldNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
