package LearningManageSystem.LMS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String userName;
    private String fullName;
    private String phone;
    private boolean active;
    private LocalDateTime lastLoginAt;
}
