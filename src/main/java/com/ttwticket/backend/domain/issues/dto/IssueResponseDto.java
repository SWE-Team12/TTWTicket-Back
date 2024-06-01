package com.ttwticket.backend.domain.issues.dto;

import com.ttwticket.backend.domain.issues.Category;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.Priority;
import com.ttwticket.backend.domain.issues.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class IssueResponseDto {
    private Integer issueId;
    private String title;
    private String description;
    private String reporter;
    private Status status;
    private Priority priority;
    private Category category;
    private List<String> assignee;
    private List<String> fixer;

    @Builder
    public IssueResponseDto(Issue issue) {
        this.issueId = issue.getIssueId();
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.reporter = issue.getReporter();
        this.status = issue.getStatus();
        this.priority = issue.getPriority();
        this.category = issue.getCategory();
        this.assignee = issue.getAssignees().stream().map(assignee -> assignee.getUser().getName()).collect(Collectors.toList());
        this.fixer = issue.getFixers().stream().map(fixer -> fixer.getUser().getName()).collect(Collectors.toList());
    }
}
