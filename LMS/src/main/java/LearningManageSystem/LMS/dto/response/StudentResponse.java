package LearningManageSystem.LMS.dto.response;

import LearningManageSystem.LMS.Entity.UserAndRole.Student;
import LearningManageSystem.LMS.Entity.UserAndRole.Student.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long id;
    private String fullName;
    private String studentCode;
    private Gender gender;
    private String address;

    public static StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .address(student.getAddress())
                .gender(student.getGender())
                .studentCode(student.getStudentCode())
                .fullName(student.getUser().getFullName())
                .build();
    }
}
