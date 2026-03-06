package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.AttitudeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttitudeRecordRepository extends JpaRepository<AttitudeRecord, Long> {
}
