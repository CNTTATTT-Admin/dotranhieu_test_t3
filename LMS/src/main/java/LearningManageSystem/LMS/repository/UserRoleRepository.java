package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.UserAndRole.UserRole;
import LearningManageSystem.LMS.Entity.UserAndRole.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
