package LearningManageSystem.LMS.Entity.Result;
import LearningManageSystem.LMS.Entity.SchoolSystem.SchoolClass;
import LearningManageSystem.LMS.Entity.SchoolSystem.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "grade_number", nullable = false, unique = true)
    private Integer gradeNumber;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    // ── Relationships ──────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
    @Builder.Default
    private List<SchoolClass> classes = new ArrayList<>();

    @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Subject> subjects = new ArrayList<>();
}

