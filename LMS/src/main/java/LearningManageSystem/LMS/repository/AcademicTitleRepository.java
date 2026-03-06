package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.AcademicTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicTitleRepository extends JpaRepository<AcademicTitle, Long> {
}
