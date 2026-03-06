package LearningManageSystem.LMS.Entity.Result;

import LearningManageSystem.LMS.Entity.SchoolSystem.Semester;
import LearningManageSystem.LMS.Entity.SchoolSystem.Subject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_configs",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_score_configs",
                columnNames = {"subject_id", "semester_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "weight_attendance", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.10")
    @Builder.Default
    private BigDecimal weightAttendance = new BigDecimal("0.10");

    @Column(name = "weight_oral", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.10")
    @Builder.Default
    private BigDecimal weightOral = new BigDecimal("0.10");

    @Column(name = "weight_15min", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.10")
    @Builder.Default
    private BigDecimal weight15min = new BigDecimal("0.10");

    @Column(name = "weight_1period", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.25")
    @Builder.Default
    private BigDecimal weight1period = new BigDecimal("0.25");

    @Column(name = "weight_midterm", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.20")
    @Builder.Default
    private BigDecimal weightMidterm = new BigDecimal("0.20");

    @Column(name = "weight_final", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.25")
    @Builder.Default
    private BigDecimal weightFinal = new BigDecimal("0.25");

    @Column(name = "absence_deduct", nullable = false, precision = 3, scale = 2,
            columnDefinition = "DECIMAL(3,2) DEFAULT 0.50")
    @Builder.Default
    private BigDecimal absenceDeduct = new BigDecimal("0.50");

    @Column(name = "base_attendance_score", nullable = false, precision = 4, scale = 2,
            columnDefinition = "DECIMAL(4,2) DEFAULT 10.00")
    @Builder.Default
    private BigDecimal baseAttendanceScore = new BigDecimal("10.00");

    @Column(name = "max_oral_columns", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    @Builder.Default
    private Integer maxOralColumns = 0;

    @Column(name = "max_15min_columns", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    @Builder.Default
    private Integer max15minColumns = 0;

    @Column(name = "max_1period_columns", nullable = false, columnDefinition = "TINYINT DEFAULT 3")
    @Builder.Default
    private Integer max1periodColumns = 3;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
