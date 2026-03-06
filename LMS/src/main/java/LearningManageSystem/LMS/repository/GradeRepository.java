package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
}
