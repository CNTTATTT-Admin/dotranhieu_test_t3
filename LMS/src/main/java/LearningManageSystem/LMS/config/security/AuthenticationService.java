package LearningManageSystem.LMS.config.security;

import LearningManageSystem.LMS.dto.request.LoginRequest;
import LearningManageSystem.LMS.dto.request.RegisterRequest;
import LearningManageSystem.LMS.dto.response.LoginResponse;
import LearningManageSystem.LMS.dto.response.RegisterResponse;
import LearningManageSystem.LMS.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service for handling authentication operations.
 * Manages user login and JWT token generation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Authenticate user with provided credentials and generate JWT token
     *
     * @param loginRequest Login credentials
     * @return Login response containing JWT token and user information
     * @throws BadRequestException If authentication fails
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user with provided credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Get authenticated user details
            CustomUserDetailsService.SecurityUserDetails userDetails = (CustomUserDetailsService.SecurityUserDetails) authentication.getPrincipal();
            log.info("User authenticated successfully: {}", userDetails.getUsername());

            // Generate JWT token
            String jwtToken = jwtService.generateToken(userDetails, userDetails.getUser().getEmail());

            // Get token expiration and issued times
            LocalDateTime expiresAt = jwtService.getExpirationDateTime(jwtToken);
            LocalDateTime issuedAt = jwtService.getIssuedAtDateTime(jwtToken);

            // Return login response with token and user information
            return LoginResponse.builder()
                    .accessToken(jwtToken)
                    .tokenType("Bearer")
                    .email(userDetails.getUsername())
                    .expiresAt(expiresAt)
                    .issuedAt(issuedAt)
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", e);
            throw new BadRequestException("Invalid email or password");
        }
    }

    /**
     * Validate JWT token
     *
     * @param token JWT token to validate
     * @return True if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extract username from JWT token
     *
     * @param token JWT token
     * @return Username/email from token
     */
    public String extractUsername(String token) {
        return jwtService.extractUsername(token);
    }
}
