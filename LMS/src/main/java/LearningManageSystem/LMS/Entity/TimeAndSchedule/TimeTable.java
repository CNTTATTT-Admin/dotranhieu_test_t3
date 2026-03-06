package LearningManageSystem.LMS.Entity.TimeAndSchedule;
import LearningManageSystem.LMS.Entity.SchoolSystem.Room;
import LearningManageSystem.LMS.Entity.SchoolSystem.SchoolClass;
import LearningManageSystem.LMS.Entity.SchoolSystem.Semester;
import LearningManageSystem.LMS.Entity.SchoolSystem.Subject;
import LearningManageSystem.LMS.Entity.UserAndRole.Teacher;
import LearningManageSystem.LMS.Entity.UserAndRole.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "timetables",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_timetable_class",
                        columnNames = {"semester_id", "class_id", "time_slot_id", "day_of_week", "week_type", "effective_from"}),
                @UniqueConstraint(name = "uq_timetable_teacher",
                        columnNames = {"semester_id", "teacher_id", "time_slot_id", "day_of_week", "week_type", "effective_from"}),
                @UniqueConstraint(name = "uq_timetable_room",
                        columnNames = {"semester_id", "room_id", "time_slot_id", "day_of_week", "week_type", "effective_from"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTable {

    public enum WeekType {
        ALL, ODD, EVEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;

    /**
     * 2=Thứ Hai, 3=Thứ Ba, 4=Thứ Tư, 5=Thứ Năm,
     * 6=Thứ Sáu, 7=Thứ Bảy, 8=Chủ Nhật
     */
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_type", nullable = false,
            columnDefinition = "ENUM('ALL','ODD','EVEN') DEFAULT 'ALL'")
    @Builder.Default
    private WeekType weekType = WeekType.ALL;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

