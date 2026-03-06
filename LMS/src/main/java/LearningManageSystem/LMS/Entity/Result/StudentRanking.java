package LearningManageSystem.LMS.Entity.Result;
import LearningManageSystem.LMS.Entity.SchoolSystem.Semester;
import LearningManageSystem.LMS.Entity.UserAndRole.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_rankings",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_student_rankings",
                columnNames = {"student_id", "semester_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRanking {

    public enum AcademicClassification {
        EXCELLENT,  // Giỏi  >= 8.0, không có môn < 6.5
        GOOD,       // Khá   6.5 - 7.9, không có môn < 5.0
        AVERAGE,    // TB    5.0 - 6.4, không có môn < 3.5
        WEAK,       // Yếu   3.5 - 4.9
        POOR        // Kém   < 3.5 hoặc có môn điểm liệt
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    /** Điểm TB chung có trọng số hệ số môn */
    @Column(name = "gpa", precision = 4, scale = 2)
    private BigDecimal gpa;

    @Column(name = "rank_in_class")
    private Integer rankInClass;

    @Column(name = "total_students")
    private Integer totalStudents;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_classification",
            columnDefinition = "ENUM('EXCELLENT','GOOD','AVERAGE','WEAK','POOR')")
    private AcademicClassification academicClassification;

    @UpdateTimestamp
    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;
}
