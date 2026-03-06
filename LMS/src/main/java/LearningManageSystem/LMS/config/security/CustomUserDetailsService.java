package LearningManageSystem.LMS.config.security;

import LearningManageSystem.LMS.Entity.UserAndRole.User;
import LearningManageSystem.LMS.repository.UserRepository;
import LearningManageSystem.LMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService implementation for loading user details from database.
 * Implements Spring Security's UserDetailsService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load user by username (email)
     *
     * @param username User's email or username
     * @return UserDetails containing user information and authorities
     * @throws UsernameNotFoundException If user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        log.debug("User found with username: {}", username);
        return new SecurityUserDetails(user);
    }

    /**
     * Load user by email
     *
     * @param email User's email
     * @return UserDetails containing user information and authorities
     * @throws UsernameNotFoundException If user not found
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        log.debug("User found with email: {}", email);
        return new SecurityUserDetails(user);
    }

    /**
     * Inner class implementing UserDetails interface for Spring Security
     */
    public static class SecurityUserDetails implements UserDetails {

        private final User user;

        public SecurityUserDetails(User user) {
            this.user = user;
        }

        /**
         * Get user authorities/roles
         *
         * @return Collection of granted authorities
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            if (user.getUserRoles() == null || user.getUserRoles().isEmpty()) {
                return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            }

            return user.getUserRoles().stream()
                    .map(userRole -> new SimpleGrantedAuthority(
                            "ROLE_" + userRole.getRole().getName()
                    ))
                    .collect(Collectors.toSet());
        }

        @Override
        public String getPassword() {
            return user.getPasswordHash();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.isActive();
        }

        /**
         * Get the underlying User entity
         *
         * @return User entity
         */
        public User getUser() {
            return user;
        }

        /**
         * Get user ID
         *
         * @return User ID
         */
        public Long getUserId() {
            return user.getId();
        }
    }
}
