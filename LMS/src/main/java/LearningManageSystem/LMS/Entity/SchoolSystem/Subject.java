package LearningManageSystem.LMS.Entity.SchoolSystem;

import LearningManageSystem.LMS.Entity.Assigment.TeachingAssignment;
import LearningManageSystem.LMS.Entity.Result.Grade;
import LearningManageSystem.LMS.Entity.Result.ScoreConfig;
import LearningManageSystem.LMS.Entity.TimeAndSchedule.TimeTable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    private String subjectCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "coefficient", nullable = false, precision = 3, scale = 1,
            columnDefinition = "DECIMAL(3,1) DEFAULT 1.0")
    @Builder.Default
    private BigDecimal coefficient = BigDecimal.ONE;

    @Column(name = "periods_per_week", nullable = false, columnDefinition = "TINYINT DEFAULT 3")
    @Builder.Default
    private Integer periodsPerWeek = 3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TeachingAssignment> teachingAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ScoreConfig> scoreConfigs = new ArrayList<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TimeTable> timetables = new ArrayList<>();
}
