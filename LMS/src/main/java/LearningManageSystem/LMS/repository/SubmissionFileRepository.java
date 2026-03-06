package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Assigment.SubmissionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionFileRepository extends JpaRepository<SubmissionFile, Long> {
}
