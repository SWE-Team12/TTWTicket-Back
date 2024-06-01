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
    private Category category;
    private Integer userId;

    @Builder
    public IssueCreateRequestDto(String title, String description, Priority priority, Category category, Integer userId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.userId = userId;
    }
    public Issue toEntity(Project project, String reporter) {
        Priority finalPriority = (priority == null) ? Priority.major : priority;
        return Issue.builder()
                .title(title)
                .description(description)
                .reporter(reporter)
                .status(Status.NEW)
                .priority(finalPriority)
                .category(category)
                .userId(userId)
                .project(project)
                .build();
    }

}
