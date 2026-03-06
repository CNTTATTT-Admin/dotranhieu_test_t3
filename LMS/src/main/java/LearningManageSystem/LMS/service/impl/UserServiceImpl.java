package LearningManageSystem.LMS.service.impl;

import LearningManageSystem.LMS.Entity.UserAndRole.User;
import LearningManageSystem.LMS.Entity.UserAndRole.Role;
import LearningManageSystem.LMS.Entity.UserAndRole.UserRole;
import LearningManageSystem.LMS.Entity.UserAndRole.UserRoleId;
import LearningManageSystem.LMS.dto.request.UpdateUserRequest;
import LearningManageSystem.LMS.dto.response.StudentResponse;
import LearningManageSystem.LMS.dto.response.UserResponse;
import LearningManageSystem.LMS.dto.request.RegisterRequest;
import LearningManageSystem.LMS.dto.response.RegisterResponse;
import LearningManageSystem.LMS.repository.StudentRepository;
import LearningManageSystem.LMS.repository.UserRepository;
import LearningManageSystem.LMS.repository.RoleRepository;
import LearningManageSystem.LMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        // validate uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUserName())) {
            throw new RuntimeException("Username already registered");
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setActive(true);

        // assign default student role
        Role defaultRole = roleRepository.findByName(Role.RoleName.STUDENT)
                .orElseThrow(() -> new RuntimeException("Default role not configured"));
        UserRole userRole = new UserRole();
        UserRoleId roleId = new UserRoleId();
        roleId.setUserId(user.getId());
        roleId.setRoleId(defaultRole.getId());
        userRole.setId(roleId);
        userRole.setUser(user);
        userRole.setRole(defaultRole);
        user.getUserRoles().add(userRole);

        User saved = userRepository.save(user);
        return mapToRegisterResponse(saved);
    }

    // existing CRUD methods from previous implementation
    @Override
    public UserResponse create(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUserName());
        user.setPasswordHash(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setActive(true);
        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

//    @Override
//    public UserResponse update(Long id, UpdateUserRequest request) {
//        User existing = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//        existing.setUsername(request.getUserName());
//        existing.setEmail(request.getEmail());
//        existing.setFullName(request.getFullName());
//        existing.setPhone(request.getPhone());
//        existing.setAvatarUrl(request.getAvatarUrl());
//        existing.setActive(request.getActive());
//        User saved = userRepository.save(existing);
//        return mapToResponse(saved);
//    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUserName(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setPhone(user.getPhone());
        response.setActive(user.isActive());
        response.setLastLoginAt(user.getLastLoginAt());
        return response;
    }

    private RegisterResponse mapToRegisterResponse(User user) {
        return RegisterResponse.builder()
                .email(user.getEmail())
                .userName(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .build();
    }
}