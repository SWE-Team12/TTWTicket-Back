package com.ttwticket.backend.domain.projects.dto;

import com.ttwticket.backend.domain.projects.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectRequestDto {
    private String title;
    private String description;

    @Builder
    public ProjectRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public Project toEntity() {
        return Project.builder()
                .title(title)
                .description(description)
                .build();
    }
}
