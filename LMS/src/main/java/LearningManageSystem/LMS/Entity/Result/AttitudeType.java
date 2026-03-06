package LearningManageSystem.LMS.Entity.Result;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attitude_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttitudeType {

    public enum Category {
        POSITIVE, NEGATIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false,
            columnDefinition = "ENUM('POSITIVE','NEGATIVE')")
    private Category category;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "default_points", nullable = false, precision = 3, scale = 1,
            columnDefinition = "DECIMAL(3,1) DEFAULT 0.5")
    @Builder.Default
    private BigDecimal defaultPoints = new BigDecimal("0.5");

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Builder.Default
    private boolean isActive = true;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "attitudeType", fetch = FetchType.LAZY)
    @Builder.Default
    private List<AttitudeRecord> attitudeRecords = new ArrayList<>();
}

