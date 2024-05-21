package com.ttwticket.backend.domain.projects.service;

import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.dto.ProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


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

    @Transactional
    public Integer modifyProject(Integer projectId, ProjectRequestDto projectRequestDto) {
        projectRepository.findByProjectId(projectId).modifyProject(projectRequestDto);
        return projectId;
    }

    public ProjectResponseDto getProject(Integer projectId) {
        return ProjectResponseDto.builder()
                .project(projectRepository.findByProjectId(projectId))
                .build();
    }

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }

}
