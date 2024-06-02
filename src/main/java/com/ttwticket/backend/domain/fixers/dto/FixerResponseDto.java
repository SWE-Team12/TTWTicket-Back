package com.ttwticket.backend.domain.fixers.dto;

import com.ttwticket.backend.domain.fixers.Fixer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FixerResponseDto {
    private Integer assigneeId;
    private String name;
    private Integer issueId;

    @Builder
    public FixerResponseDto(Fixer fixer) {
        this.assigneeId = fixer.getFixerId();
        this.name = fixer.getUser().getName();
        this.issueId = fixer.getIssue().getIssueId();
    }
}
