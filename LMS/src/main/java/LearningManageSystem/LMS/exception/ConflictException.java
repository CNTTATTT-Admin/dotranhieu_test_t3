package LearningManageSystem.LMS.exception;

/**
 * Exception thrown when a conflict is detected (e.g., duplicate resource, state conflict).
 * This typically results in a 409 CONFLICT HTTP response.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
