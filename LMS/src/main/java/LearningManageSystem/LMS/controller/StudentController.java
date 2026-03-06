package LearningManageSystem.LMS.controller;

import LearningManageSystem.LMS.config.security.CustomUserDetailsService;
import LearningManageSystem.LMS.config.security.CustomUserDetailsService.SecurityUserDetails;
import LearningManageSystem.LMS.dto.response.ApiResponse;
import LearningManageSystem.LMS.dto.response.StudentResponse;
import LearningManageSystem.LMS.service.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/class")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudentsByClassId(){
        SecurityUserDetails userDetails =
                (CustomUserDetailsService.SecurityUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Long homeRoomTeacherId = userDetails.getUser().getId();
        return ResponseEntity.ok(ApiResponse.<List<StudentResponse>>builder()
                .data(studentService.getAllStudentsByClassId(homeRoomTeacherId))
                .message("Success")
                .statusCode(HttpStatus.OK.value())
                .build());
    }
}
