package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Assigment.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
}
