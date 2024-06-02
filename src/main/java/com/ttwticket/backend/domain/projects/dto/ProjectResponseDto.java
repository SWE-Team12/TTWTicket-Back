package com.ttwticket.backend.domain.projects.dto;

import com.ttwticket.backend.domain.projects.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponseDto {
    private Integer projectId;
    private String title;
    private String description;

    @Builder
    public ProjectResponseDto(Project project) {
        this.projectId = project.getProjectId();
        this.title = project.getTitle();
        this.description = project.getDescription();
    }
}
