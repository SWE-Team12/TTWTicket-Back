package com.ttwticket.backend.domain.issues.dto;

import com.ttwticket.backend.domain.issues.*;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueRequestDto {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    //private String comment;

    @Builder
    public IssueRequestDto(String title, String description, Status status, Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        // this.comment = comment;
    }
    public Issue toEntity() {
        return Issue.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                //.comment(comment)
                .build();
    }

}
