package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.ConductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductRatingRepository extends JpaRepository<ConductRating, Long> {
}
