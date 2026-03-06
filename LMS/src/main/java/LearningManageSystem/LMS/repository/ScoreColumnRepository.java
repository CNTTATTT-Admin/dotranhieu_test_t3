package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.ScoreColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreColumnRepository extends JpaRepository<ScoreColumn, Long> {
}
