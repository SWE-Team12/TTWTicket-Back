package com.ttwticket.backend.domain.admin.service;

import com.ttwticket.backend.domain.admin.dto.AdminRequestDto;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import com.ttwticket.backend.domain.users.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;

//    @Transactional
//    public UserIdResponseDto assignUser(AdminRequestDto adminRequestDto) {
//        User user = userRepository.findByEmailAndIsDeleted(adminRequestDto.getEmail(), false);
//        user.setProjectId(adminRequestDto.getProjectId());
//
//        UserIdResponseDto userIdResponseDto = UserIdResponseDto.builder()
//                .userId(userRepository.save(user).getUserId())
//                .build();
//
//        return userIdResponseDto;
//
//    }

    @Transactional
    public ProjectIdResponseDto createProject(ProjectRequestDto projectRequestDto) {
        ProjectIdResponseDto projectIdResponseDto = ProjectIdResponseDto.builder()
                .projectId(projectRepository.save(projectRequestDto.toEntity()).getProjectId())
                .build();
        return projectIdResponseDto;
    }

    @Transactional
    public UserIdResponseDto registerUser(UserRequestDto userRequestDto)  {
        String encodedPassword = encoder.encode(userRequestDto.getPassword());
        Project project = projectValid(userRequestDto.getProjectId());

        UserIdResponseDto userIdResponseDto = UserIdResponseDto.builder()
                .userId(userRepository.save(userRequestDto.toEntity(encodedPassword, project)).getUserId())
                .build();
        return userIdResponseDto;
    }

    public Project projectValid(Integer projectId) {
        return projectRepository.findByProjectId(projectId);
    }
}