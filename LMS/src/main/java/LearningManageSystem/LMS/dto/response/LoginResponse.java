package LearningManageSystem.LMS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for login response.
 * Contains JWT token and user information after successful authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    /**
     * JWT access token for subsequent API requests
     */
    private String accessToken;

    /**
     * Token type (typically "Bearer")
     */
    private String tokenType;

    /**
     * User's unique identifier
     */
    private Long userId;

    /**
     * User's email/username
     */
    private String email;

    /**
     * User's full name
     */
    private String fullName;

    /**
     * Token expiration time
     */
    private LocalDateTime expiresAt;

    /**
     * Timestamp when token was issued
     */
    private LocalDateTime issuedAt;
}
