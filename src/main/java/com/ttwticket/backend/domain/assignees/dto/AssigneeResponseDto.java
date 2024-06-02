package com.ttwticket.backend.domain.assignees.dto;

import com.ttwticket.backend.domain.assignees.Assignee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssigneeResponseDto {
    private Integer assigneeId;
    private String name;
    private Integer issueId;

    @Builder
    public AssigneeResponseDto(Assignee assignee) {
        this.assigneeId = assignee.getAssigneeId();
        this.name = assignee.getUser().getName();
        this.issueId = assignee.getIssue().getIssueId();
    }
}
