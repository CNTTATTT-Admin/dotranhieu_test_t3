package LearningManageSystem.LMS.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for JWT token generation, validation, and extraction.
 * Handles all JWT-related operations including token creation and claims extraction.
 */
@Service
@Slf4j
public class JwtService {

    @Value("${app.jwt.secret:mySecretKeyForJWTTokenGenerationAndValidationPurposesOnly123456789}")
    private String secretKey;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    /**
     * Generate JWT token from user details
     *
     * @param userDetails User details containing username and authorities
     * @return Generated JWT token
     */
    public String generateToken(UserDetails userDetails, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Generate JWT token with additional claims
     *
     * @param extraClaims Additional claims to include in the token
     * @param userDetails User details containing username and authorities
     * @return Generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Create JWT token with claims and subject
     *
     * @param claims JWT claims
     * @param subject Subject (usually username/email)
     * @return Generated JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expiryDate = new Date(currentTimeMillis + jwtExpirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username/email from JWT token
     *
     * @param token JWT token
     * @return Username/email extracted from token
     */
    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return getAllClaims(token).get("email", String.class);
    }

    /**
     * Check if JWT token is valid for a given user
     *
     * @param token JWT token
     * @param userDetails User details to validate against
     * @return True if token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Check if JWT token is expired
     *
     * @param token JWT token
     * @return True if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date from JWT token
     *
     * @param token JWT token
     * @return Expiration date of the token
     */
    private Date extractExpiration(String token) {
        return getAllClaims(token).getExpiration();
    }

    /**
     * Extract all claims from JWT token
     *
     * @param token JWT token
     * @return Claims object containing all token claims
     */
    private Claims getAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims from JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Get the signing key for JWT
     *
     * @return Signing key
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Get token expiration time as LocalDateTime
     *
     * @param token JWT token
     * @return Expiration time as LocalDateTime
     */
    public LocalDateTime getExpirationDateTime(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Get token issued time as LocalDateTime
     *
     * @param token JWT token
     * @return Issued time as LocalDateTime
     */
    public LocalDateTime getIssuedAtDateTime(String token) {
        return getAllClaims(token).getIssuedAt().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
