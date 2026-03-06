package LearningManageSystem.LMS.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "UserName is required")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "FullName is required")
    private String fullName;

    @NotBlank(message = "Phone is required")
    private String phone;
}
