package com.ttwticket.backend.domain.issues.dto;

import com.ttwticket.backend.domain.issues.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueStatusChangeRequestDto {
    private Status status;

    @Builder
    public IssueStatusChangeRequestDto(Status status) {
        this.status = status;
    }
}
