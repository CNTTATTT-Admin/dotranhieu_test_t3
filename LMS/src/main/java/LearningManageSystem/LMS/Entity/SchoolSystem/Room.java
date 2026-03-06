package LearningManageSystem.LMS.Entity.SchoolSystem;
import LearningManageSystem.LMS.Entity.TimeAndSchedule.TimeTable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    public enum RoomType {
        CLASSROOM, LAB, GYM, HALL, OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "room_code", nullable = false, unique = true, length = 20)
    private String roomCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "capacity", nullable = false, columnDefinition = "TINYINT DEFAULT 45")
    @Builder.Default
    private Integer capacity = 45;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false,
            columnDefinition = "ENUM('CLASSROOM','LAB','GYM','HALL','OTHER') DEFAULT 'CLASSROOM'")
    @Builder.Default
    private RoomType roomType = RoomType.CLASSROOM;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "building", length = 50)
    private String building;

    @Column(name = "has_projector", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private boolean hasProjector = false;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SchoolClass> classes = new ArrayList<>();

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TimeTable> timetables = new ArrayList<>();
}