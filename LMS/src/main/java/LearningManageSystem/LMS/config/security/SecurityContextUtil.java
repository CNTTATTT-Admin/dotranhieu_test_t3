package LearningManageSystem.LMS.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for accessing current authentication and user information.
 * Provides convenient methods to get authenticated user details from SecurityContext.
 */
@Component
@Slf4j
public class SecurityContextUtil {

    /**
     * Get the current authenticated user's username/email
     *
     * @return Username of the authenticated user, or null if not authenticated
     */
    public static String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Get the current Authentication object
     *
     * @return Current Authentication object from SecurityContextHolder
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Check if user is currently authenticated
     *
     * @return True if user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * Get the current authenticated user's UserDetails
     *
     * @return UserDetails of authenticated user
     */
    public static Object getCurrentUserPrincipal() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Get the current authenticated user as SecurityUserDetails
     *
     * @return SecurityUserDetails if user is authenticated, null otherwise
     */
    public static CustomUserDetailsService.SecurityUserDetails getCurrentSecurityUser() {
        Object principal = getCurrentUserPrincipal();
        if (principal instanceof CustomUserDetailsService.SecurityUserDetails) {
            return (CustomUserDetailsService.SecurityUserDetails) principal;
        }
        return null;
    }

    /**
     * Get the current authenticated user's ID
     *
     * @return User ID if authenticated, null otherwise
     */
    public static Long getCurrentUserId() {
        CustomUserDetailsService.SecurityUserDetails userDetails = getCurrentSecurityUser();
        if (userDetails != null) {
            return userDetails.getUserId();
        }
        return null;
    }
}
