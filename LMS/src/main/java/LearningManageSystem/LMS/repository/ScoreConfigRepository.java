package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.ScoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreConfigRepository extends JpaRepository<ScoreConfig, Integer> {
}
