package vnavesnoj.stomp_status_ping.exception;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class InvalidSessionToUpdateException extends RuntimeException {

    public InvalidSessionToUpdateException() {
    }

    public InvalidSessionToUpdateException(String message) {
        super(message);
    }

    public InvalidSessionToUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSessionToUpdateException(Throwable cause) {
        super(cause);
    }

    public InvalidSessionToUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
