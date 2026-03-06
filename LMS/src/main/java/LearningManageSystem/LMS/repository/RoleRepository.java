package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.UserAndRole.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Find role by its enum name
     */
    Optional<Role> findByName(Role.RoleName name);
}
