package com.ttwticket.backend.domain.issues.dto;

import com.ttwticket.backend.domain.issues.*;

import com.ttwticket.backend.domain.projects.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueCreateRequestDto {
    private String title;
    private String description;
    private Priority priority;
    private Integer userId;

    @Builder
    public IssueCreateRequestDto(String title, String description, Priority priority, Integer userId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.userId = userId;
    }
    public Issue toEntity(Project project, String reporter) {
        return Issue.builder()
                .title(title)
                .description(description)
                .reporter(reporter)
                .status(Status.NEW)
                .priority(priority)
                .userId(userId)
                .project(project)
                .build();
    }

}
