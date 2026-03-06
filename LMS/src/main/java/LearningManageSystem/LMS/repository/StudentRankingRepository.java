package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.StudentRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRankingRepository extends JpaRepository<StudentRanking, Long> {
}
