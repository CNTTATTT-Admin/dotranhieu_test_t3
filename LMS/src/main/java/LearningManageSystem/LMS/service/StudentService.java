package LearningManageSystem.LMS.service;

import LearningManageSystem.LMS.Entity.Result.AcademicTitle.Title;
import LearningManageSystem.LMS.Entity.Result.ConductRating.Rating;
import LearningManageSystem.LMS.Entity.Result.ScoreColumn.ScoreType;
import LearningManageSystem.LMS.Entity.Result.StudentRanking.AcademicClassification;
import LearningManageSystem.LMS.dto.response.PageResponse;
import LearningManageSystem.LMS.dto.response.StudentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    List<StudentResponse> getAllStudentsByClassId(Long classId);
    List<StudentResponse> getAllStudnetByGroupId(Long groupId);
    PageResponse<StudentResponse> getAllStudentByConductRating(Rating rating, Pageable pageable);
    PageResponse<StudentResponse> getAllStudentByAcademicTitle(Title title, Pageable pageable);
    List<StudentResponse> getAllStudentByClassIdAndScoreColumn(Long classId, ScoreType scoreType);
    List<StudentResponse> getAllStudentByClassIdAndRanking(Long classId, AcademicClassification fiter);
}
