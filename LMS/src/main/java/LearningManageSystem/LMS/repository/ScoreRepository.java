package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
}
