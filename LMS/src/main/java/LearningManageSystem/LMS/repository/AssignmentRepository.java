package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Assigment.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
