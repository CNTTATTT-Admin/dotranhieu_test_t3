package LearningManageSystem.LMS.Entity.UserAndRole;

import LearningManageSystem.LMS.Entity.Assigment.TeachingAssignment;
import LearningManageSystem.LMS.Entity.SchoolSystem.SchoolClass;
import LearningManageSystem.LMS.Entity.TimeAndSchedule.TimeTable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    public enum Degree {
        BACHELOR, MASTER, DOCTOR, PROFESSOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "teacher_code", nullable = false, unique = true, length = 20)
    private String teacherCode;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "specialization", length = 200)
    private String specialization;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree", columnDefinition = "ENUM('BACHELOR','MASTER','DOCTOR','PROFESSOR')")
    private Degree degree;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "is_homeroom", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private boolean isHomeroom = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "homeroomTeacher", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SchoolClass> homeroomClasses = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TeachingAssignment> teachingAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TimeTable> timetables = new ArrayList<>();
}
