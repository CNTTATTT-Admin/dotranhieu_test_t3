package LearningManageSystem.LMS.Entity.Assigment;

import LearningManageSystem.LMS.Entity.Result.Attendance;
import LearningManageSystem.LMS.Entity.Result.AttitudeRecord;
import LearningManageSystem.LMS.Entity.Result.GradeResult;
import LearningManageSystem.LMS.Entity.Result.ScoreColumn;
import LearningManageSystem.LMS.Entity.SchoolSystem.SchoolClass;
import LearningManageSystem.LMS.Entity.SchoolSystem.Semester;
import LearningManageSystem.LMS.Entity.SchoolSystem.Subject;
import LearningManageSystem.LMS.Entity.UserAndRole.Teacher;
import LearningManageSystem.LMS.Entity.UserAndRole.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teaching_assignments",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_teaching_assignment",
                columnNames = {"teacher_id", "subject_id", "class_id", "semester_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = false)
    private User assignedBy;

    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean isActive = true;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "teachingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ScoreColumn> scoreColumns = new ArrayList<>();

    @OneToMany(mappedBy = "teachingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "teachingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AttitudeRecord> attitudeRecords = new ArrayList<>();

    @OneToMany(mappedBy = "teachingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "teachingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GradeResult> gradeResults = new ArrayList<>();
}
