package com.ttwticket.backend.domain.projects.service;

import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectIdResponseDto createProject(ProjectRequestDto projectRequestDto) {
        ProjectIdResponseDto projectIdResponseDto = ProjectIdResponseDto.builder()
                .projectId(projectRepository.save(projectRequestDto.toEntity()).getProjectId())
                .build();
        return projectIdResponseDto;
    }
}
