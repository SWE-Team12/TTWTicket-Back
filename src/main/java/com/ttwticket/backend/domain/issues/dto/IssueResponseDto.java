package com.ttwticket.backend.domain.issues.dto;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.Priority;
import com.ttwticket.backend.domain.issues.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssueResponseDto {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    //private String comment;

    @Builder
    public IssueResponseDto(Issue issue) {
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.status = issue.getStatus();
        this.priority = issue.getPriority();
    }

}
