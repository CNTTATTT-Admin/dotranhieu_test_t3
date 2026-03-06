package LearningManageSystem.LMS.Entity.SchoolSystem;

import LearningManageSystem.LMS.Entity.Assigment.TeachingAssignment;
import LearningManageSystem.LMS.Entity.Result.Grade;
import LearningManageSystem.LMS.Entity.TimeAndSchedule.TimeTable;
import LearningManageSystem.LMS.Entity.UserAndRole.Student;
import LearningManageSystem.LMS.Entity.UserAndRole.Teacher;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_classes_code_year",
                columnNames = {"class_code", "academic_year_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "class_code", nullable = false, length = 20)
    private String classCode;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private Teacher homeroomTeacher;

    @Column(name = "max_students", nullable = false, columnDefinition = "TINYINT DEFAULT 45")
    @Builder.Default
    private Integer maxStudents = 45;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TeachingAssignment> teachingAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "schoolClass", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TimeTable> timetables = new ArrayList<>();
}
