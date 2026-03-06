package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.UserAndRole.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     *
     * @param username User's username
     * @return Optional containing user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * @param email User's email
     * @return Optional containing user if found
     */
    @Query("""
       SELECT u FROM User u
       LEFT JOIN FETCH u.userRoles ur
       LEFT JOIN FETCH ur.role
       WHERE u.email = :email
       """)
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Check if user exists by username
     *
     * @param username User's username
     * @return True if user exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if user exists by email
     *
     * @param email User's email
     * @return True if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}
