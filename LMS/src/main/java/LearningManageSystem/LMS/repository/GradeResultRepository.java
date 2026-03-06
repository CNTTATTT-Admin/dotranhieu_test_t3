package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.GradeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeResultRepository extends JpaRepository<GradeResult, Long> {
}
