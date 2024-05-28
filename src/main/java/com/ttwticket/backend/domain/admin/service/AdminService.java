package com.ttwticket.backend.domain.admin.service;

import com.ttwticket.backend.domain.admin.dto.AdminRequestDto;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final ProjectRepository projectRepository;

    @Transactional
    public Project assignUser(AdminRequestDto adminRequestDto, Integer projectId) {
        Project project = projectValid(projectId);
        User user = userService.findUserByEmail(adminRequestDto.getEmail());

        User newUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

        project.getUsers().add(newUser);

        return projectRepository.save(project);
    }

    public Project projectValid(Integer projectId) {
        return projectRepository.findByProjectId(projectId);
    }


}
