package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.TimeAndSchedule.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
