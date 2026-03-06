package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
