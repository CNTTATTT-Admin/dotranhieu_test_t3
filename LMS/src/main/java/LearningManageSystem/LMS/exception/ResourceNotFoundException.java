package LearningManageSystem.LMS.exception;

/**
 * Exception thrown when a requested resource is not found in the database.
 * This typically results in a 404 NOT_FOUND HTTP response.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue, Throwable cause) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue), cause);
    }
}
