package com.ttwticket.backend.domain.issues.dto;

import com.ttwticket.backend.domain.issues.*;

import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.users.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueRequestDto {
    private String title;
    private String description;
    //private String reporter;
    //private Status status;
    private Priority priority;
    private Integer userId;

//    public void setReporter(String reporter) {
//        this.reporter = reporter;
//    }

    @Builder
    public IssueRequestDto(String title, String description, String reporter, Status status, Priority priority, Integer userId) {
        this.title = title;
        this.description = description;
        //this.reporter = reporter;
        //this.status = status;
        this.priority = priority;
        this.userId = userId;
    }
    public Issue toEntity(String reporter, Project project) {
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