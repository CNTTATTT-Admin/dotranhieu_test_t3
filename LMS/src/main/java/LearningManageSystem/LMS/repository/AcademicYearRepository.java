package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.SchoolSystem.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Integer> {
}
