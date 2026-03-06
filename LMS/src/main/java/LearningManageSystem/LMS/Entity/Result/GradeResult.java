package LearningManageSystem.LMS.Entity.Result;

import LearningManageSystem.LMS.Entity.Assigment.TeachingAssignment;
import LearningManageSystem.LMS.Entity.UserAndRole.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grade_results",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_grade_results",
                columnNames = {"student_id", "teaching_assignment_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResult {

    public enum Classification {
        EXCELLENT,  // Giỏi  >= 8.0
        GOOD,       // Khá   6.5 - 7.9
        AVERAGE,    // TB    5.0 - 6.4
        WEAK,       // Yếu   3.5 - 4.9
        POOR        // Kém   < 3.5
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assignment_id", nullable = false)
    private TeachingAssignment teachingAssignment;

    @Column(name = "attendance_score", precision = 4, scale = 2)
    private BigDecimal attendanceScore;

    @Column(name = "oral_avg", precision = 4, scale = 2)
    private BigDecimal oralAvg;

    @Column(name = "min15_avg", precision = 4, scale = 2)
    private BigDecimal min15Avg;

    @Column(name = "period1_avg", precision = 4, scale = 2)
    private BigDecimal period1Avg;

    @Column(name = "midterm_score", precision = 4, scale = 2)
    private BigDecimal midtermScore;

    @Column(name = "final_score", precision = 4, scale = 2)
    private BigDecimal finalScore;

    /** Điểm trung bình môn, đã làm tròn 0.5 */
    @Column(name = "average_score", precision = 4, scale = 2)
    private BigDecimal averageScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "classification",
            columnDefinition = "ENUM('EXCELLENT','GOOD','AVERAGE','WEAK','POOR')")
    private Classification classification;

    @Column(name = "class_rank")
    private Integer classRank;

    @UpdateTimestamp
    @Column(name = "recalculated_at", nullable = false)
    private LocalDateTime recalculatedAt;
}
