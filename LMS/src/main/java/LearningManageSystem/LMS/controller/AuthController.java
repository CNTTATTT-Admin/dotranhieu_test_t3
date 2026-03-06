package LearningManageSystem.LMS.controller;

import LearningManageSystem.LMS.config.security.AuthenticationService;
import LearningManageSystem.LMS.dto.request.LoginRequest;
import LearningManageSystem.LMS.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST Controller for authentication endpoints.
 * Handles user login and token generation.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticate user and generate JWT token
     *
     * @param loginRequest User credentials (email and password)
     * @return Login response containing JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        
        LoginResponse response = authenticationService.login(loginRequest);
        
        log.info("User logged in successfully: {}", loginRequest.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint (optional)
     *
     * @return OK response indicating service is running
     */
    @PostMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Authentication service is running");
    }
}
