package LearningManageSystem.LMS.Entity.Assigment;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    public enum AssignmentType {
        HOMEWORK, PROJECT, EXERCISE, EXAM
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assignment_id", nullable = false)
    private TeachingAssignment teachingAssignment;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_type", nullable = false,
            columnDefinition = "ENUM('HOMEWORK','PROJECT','EXERCISE','EXAM') DEFAULT 'HOMEWORK'")
    @Builder.Default
    private AssignmentType assignmentType = AssignmentType.HOMEWORK;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "max_score", precision = 4, scale = 1,
            columnDefinition = "DECIMAL(4,1) DEFAULT 10.0")
    @Builder.Default
    private BigDecimal maxScore = new BigDecimal("10.0");

    @Column(name = "allow_late", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private boolean allowLate = false;

    @Column(name = "late_penalty_per_day", nullable = false, precision = 3, scale = 1,
            columnDefinition = "DECIMAL(3,1) DEFAULT 0")
    @Builder.Default
    private BigDecimal latePenaltyPerDay = BigDecimal.ZERO;

    @Column(name = "allow_file_upload", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean allowFileUpload = true;

    @Column(name = "allow_text_submission", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean allowTextSubmission = true;

    @Column(name = "max_file_size_mb", nullable = false, columnDefinition = "INT DEFAULT 50")
    @Builder.Default
    private Integer maxFileSizeMb = 50;

    @Column(name = "allowed_file_types", length = 200)
    private String allowedFileTypes;

    @Column(name = "is_published", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private boolean isPublished = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AssignmentSubmission> submissions = new ArrayList<>();
}
