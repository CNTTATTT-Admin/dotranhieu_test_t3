package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.UserAndRole.ParentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentInfoRepository extends JpaRepository<ParentInfo, Long> {
}
