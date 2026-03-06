package LearningManageSystem.LMS.Entity.Result;

import LearningManageSystem.LMS.Entity.Assigment.TeachingAssignment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "score_columns",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_score_column",
                columnNames = {"teaching_assignment_id", "score_type", "column_number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreColumn {

    public enum ScoreType {
        ATTENDANCE,  // Chuyên cần (tự động)
        ORAL,        // Kiểm tra miệng
        MIN15,       // Kiểm tra 15 phút
        PERIOD1,     // Kiểm tra 1 tiết
        MIDTERM,     // Giữa kỳ
        FINAL        // Cuối kỳ
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assignment_id", nullable = false)
    private TeachingAssignment teachingAssignment;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_type", nullable = false,
            columnDefinition = "ENUM('ATTENDANCE','ORAL','MIN15','PERIOD1','MIDTERM','FINAL')")
    private ScoreType scoreType;

    @Column(name = "column_number", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    @Builder.Default
    private Integer columnNumber = 1;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(name = "max_score", nullable = false, precision = 4, scale = 1,
            columnDefinition = "DECIMAL(4,1) DEFAULT 10.0")
    @Builder.Default
    private BigDecimal maxScore = new BigDecimal("10.0");

    @Column(name = "is_locked", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private boolean isLocked = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "scoreColumn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Score> scores = new ArrayList<>();
}
