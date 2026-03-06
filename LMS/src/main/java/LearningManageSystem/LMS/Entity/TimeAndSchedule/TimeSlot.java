package LearningManageSystem.LMS.Entity.TimeAndSchedule;

import LearningManageSystem.LMS.Entity.Result.Attendance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {

    public enum Shift {
        MORNING, AFTERNOON
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "slot_number", nullable = false, unique = true)
    private Integer slotNumber;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift", nullable = false,
            columnDefinition = "ENUM('MORNING','AFTERNOON') DEFAULT 'MORNING'")
    @Builder.Default
    private Shift shift = Shift.MORNING;

    @Column(name = "description", length = 100)
    private String description;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TimeTable> timetables = new ArrayList<>();

    @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Attendance> attendances = new ArrayList<>();
}

