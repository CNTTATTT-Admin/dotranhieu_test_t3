package LearningManageSystem.LMS.Entity.Result;

import LearningManageSystem.LMS.Entity.SchoolSystem.Semester;
import LearningManageSystem.LMS.Entity.UserAndRole.Student;
import LearningManageSystem.LMS.Entity.UserAndRole.Teacher;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "conduct_ratings",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_conduct_ratings",
                columnNames = {"student_id", "semester_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConductRating {

    public enum Rating {
        EXCELLENT,  // Tốt
        GOOD,       // Khá
        AVERAGE,    // Trung bình
        WEAK        // Yếu
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false,
            columnDefinition = "ENUM('EXCELLENT','GOOD','AVERAGE','WEAK')")
    private Rating rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_by", nullable = false)
    private Teacher ratedBy;

    @CreationTimestamp
    @Column(name = "rated_at", nullable = false, updatable = false)
    private LocalDateTime ratedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
