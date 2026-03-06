package LearningManageSystem.LMS.controller;

import LearningManageSystem.LMS.dto.request.RegisterRequest;
import LearningManageSystem.LMS.dto.response.ApiResponse;
import LearningManageSystem.LMS.dto.response.RegisterResponse;
import LearningManageSystem.LMS.dto.response.UserResponse;
import LearningManageSystem.LMS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult)
    {
        return ResponseEntity.ok(ApiResponse.<RegisterResponse>builder()
                .data(userService.register(request))
                .message("Register successfully!")
                .statusCode(HttpStatus.CREATED.value())
                .build());
    }

    @PreAuthorize("ADMIN")
    @PostMapping("/new_user")
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created successfully!")
                .data(userService.create(request))
                .build());
    }
}
