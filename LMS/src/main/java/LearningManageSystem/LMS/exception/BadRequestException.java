package LearningManageSystem.LMS.exception;

/**
 * Exception thrown when the client sends an invalid or malformed request.
 * This typically results in a 400 BAD_REQUEST HTTP response.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
