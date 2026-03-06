package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.SchoolSystem.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

    @Query("""
            SELECT c.id FROM SchoolClass c
            WHERE c.homeroomTeacher.id = :teacherId
            """)
    Long getClassIdByHomeTeacherClassId(@Param("teacherId") Long teacherId);
}
