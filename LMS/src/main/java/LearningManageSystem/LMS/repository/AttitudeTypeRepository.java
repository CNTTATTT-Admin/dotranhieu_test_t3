package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.AttitudeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttitudeTypeRepository extends JpaRepository<AttitudeType, Long> {
}
