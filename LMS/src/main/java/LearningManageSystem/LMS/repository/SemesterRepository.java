package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.SchoolSystem.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
}
