package LearningManageSystem.LMS.service;

import LearningManageSystem.LMS.dto.request.RegisterRequest;
import LearningManageSystem.LMS.dto.response.RegisterResponse;
import LearningManageSystem.LMS.dto.request.UpdateUserRequest;
import LearningManageSystem.LMS.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    /**
     * Register a new user (student by default)
     */
    RegisterResponse register(RegisterRequest request);

    // existing CRUD operations kept for compatibility
    UserResponse create(RegisterRequest request);
//    UserResponse update(Long id, UpdateUserRequest request);
    void delete(Long id);
    UserResponse getById(Long id);
    List<UserResponse> getAll();
}