package LearningManageSystem.LMS.Entity.UserAndRole;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    public enum RoleName {
        ADMIN, TEACHER, HOMEROOM_TEACHER, STUDENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true, columnDefinition = "ENUM('ADMIN','TEACHER','HOMEROOM_TEACHER','STUDENT')")
    private RoleName name;

    @Column(name = "description", length = 255)
    private String description;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();
}
