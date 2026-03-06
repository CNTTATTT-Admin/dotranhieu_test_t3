package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.Result.AcademicTitle.Title;
import LearningManageSystem.LMS.Entity.Result.ConductRating.Rating;
import LearningManageSystem.LMS.Entity.Result.StudentRanking.AcademicClassification;
import LearningManageSystem.LMS.Entity.UserAndRole.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
            SELECT * FROM Student s
            WHERE s.schoolClass.id = :id
            """)
    List<Student> getAllByClassId(@Param("id") Long classId);


    @Query("""
            SELECT s FROM Student s
            JOIN ConductRating c
               ON c.student.id = s.id
            WHERE c.rating = :rating
            """)
    Page<Student> getAllStudentsByConductRating(
            @Param("rating") Rating rating
            , Pageable pageable);

    @Query("""
            SELECT s FROM Student s
            JOIN AcademicTitle a
               ON a.student.id = s.id
            WHERE a.title = :title
            """)
    Page<Student> getAllStudnetsByAcademicTitle(
            @Param("title") Title title,
            Pageable pageable);

    @Query("""
            SELECT s FROM Student s
            JOIN StudentRanking r
               ON r.student.id = s.id
            WHERE s.schoolClass.id = :classId
               AND r.academicClassification = :filter
            """)
    List<Student> getAllStudentsByClassIdAndRanking(
            @Param("classId") Long classId,
            AcademicClassification filter);
}
