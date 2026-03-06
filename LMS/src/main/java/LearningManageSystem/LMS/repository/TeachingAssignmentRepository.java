package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Assigment.TeachingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {
}
