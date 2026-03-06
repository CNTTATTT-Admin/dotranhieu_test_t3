package LearningManageSystem.LMS.service.impl;

import LearningManageSystem.LMS.Entity.Result.AcademicTitle.Title;
import LearningManageSystem.LMS.Entity.Result.ConductRating.Rating;
import LearningManageSystem.LMS.Entity.Result.ScoreColumn.ScoreType;
import LearningManageSystem.LMS.Entity.Result.StudentRanking.AcademicClassification;
import LearningManageSystem.LMS.dto.response.PageResponse;
import LearningManageSystem.LMS.dto.response.StudentResponse;
import LearningManageSystem.LMS.repository.SchoolClassRepository;
import LearningManageSystem.LMS.repository.StudentRepository;
import LearningManageSystem.LMS.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Override
    public List<StudentResponse> getAllStudentsByClassId(Long homeRoomTeacherId) {
        Long classId = schoolClassRepository.getClassIdByHomeTeacherClassId(homeRoomTeacherId);
        return studentRepository.getAllByClassId(classId)
                .stream()
                .map(StudentResponse::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> getAllStudnetByGroupId(Long groupId) {
        return List.of();
    }

    @Override
    public PageResponse<StudentResponse> getAllStudentByConductRating(Rating rating, Pageable pageable) {
        return PageResponse.fromPage(
                studentRepository.getAllStudentsByConductRating(rating, pageable)
                        .map(StudentResponse::mapToResponse)
        );
    }

    @Override
    public PageResponse<StudentResponse> getAllStudentByAcademicTitle(Title title, Pageable pageable) {
        return PageResponse.fromPage(
                studentRepository.getAllStudnetsByAcademicTitle(title, pageable)
                        .map(StudentResponse::mapToResponse)
        );
    }

    @Override
    public List<StudentResponse> getAllStudentByClassIdAndScoreColumn(Long classId, ScoreType scoreType) {
        return List.of();
    }

    @Override
    public List<StudentResponse> getAllStudentByClassIdAndRanking(Long classId, AcademicClassification fiter) {
        return studentRepository.getAllStudentsByClassIdAndRanking(classId, fiter)
                .stream()
                .map(StudentResponse::mapToResponse)
                .collect(Collectors.toList());
    }
}
