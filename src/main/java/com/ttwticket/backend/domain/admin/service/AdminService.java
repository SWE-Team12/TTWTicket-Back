package com.ttwticket.backend.domain.admin.service;

import com.ttwticket.backend.domain.admin.dto.AdminRequestDto;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public UserIdResponseDto assignUser(AdminRequestDto adminRequestDto) {
        User user = userService.findUserByEmail(adminRequestDto.getEmail());
        user.setProjectId(adminRequestDto.getProjectId());

        UserIdResponseDto userIdResponseDto = UserIdResponseDto.builder()
                .userId(userRepository.save(user).getUserId())
                .build();

        return userIdResponseDto;

    }

}