package LearningManageSystem.LMS.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Standard error response structure for API responses.
 * This class provides a consistent format for all error responses across the application.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    /**
     * Timestamp when the error occurred
     */
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    /**
     * HTTP status code
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * Error type or error code
     */
    @JsonProperty("error")
    private String error;

    /**
     * Detailed error message
     */
    @JsonProperty("message")
    private String message;

    /**
     * Request path that caused the error
     */
    @JsonProperty("path")
    private String path;
}
