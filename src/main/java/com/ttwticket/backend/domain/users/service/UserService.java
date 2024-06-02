package com.ttwticket.backend.domain.users.service;

import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.security.JwtTokenUtil;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.*;
import com.ttwticket.backend.global.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Transactional
    public UserIdResponseDto registerUser(UserRequestDto userRequestDto)  {
        String encodedPassword = encoder.encode(userRequestDto.getPassword());
        Project project = projectValid(userRequestDto.getProjectId());

        UserIdResponseDto userIdResponseDto = UserIdResponseDto.builder()
                .userId(userRepository.save(userRequestDto.toEntity(encodedPassword, project)).getUserId())
                .build();
        return userIdResponseDto;
    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto){
        String requestEmail = userLoginRequestDto.getEmail();
        String requestPassword = userLoginRequestDto.getPassword();

        User found = userValid(requestEmail);

        if(!encoder.matches(requestPassword, found.getPassword())) {
            throw new RuntimeException(String.valueOf(ErrorCode.INVALID_PASSWORD));
        }
        return new UserLoginResponseDto(JwtTokenUtil.createToken(requestEmail, found.getUserId(), found.getName(), secretKey));
    }

    @Transactional
    public List<UserResponseDto> getAllUsers(Integer projectId) {
        Project project = projectValid(projectId);

        return userRepository.findUsersByProject(project).stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    public User userValid(String email) {
        return userRepository.findByEmailAndIsDeleted(email, false);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeleted(email, false);
    }

    public Project projectValid(Integer projectId) {
        return projectRepository.findByProjectId(projectId);
    }
}
