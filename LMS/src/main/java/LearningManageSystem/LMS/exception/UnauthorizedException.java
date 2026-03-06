package LearningManageSystem.LMS.exception;

/**
 * Exception thrown when the user is not authenticated or lacks proper credentials.
 * This typically results in a 401 UNAUTHORIZED HTTP response.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
