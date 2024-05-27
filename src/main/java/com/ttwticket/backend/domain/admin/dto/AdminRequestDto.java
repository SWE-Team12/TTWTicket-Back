package com.ttwticket.backend.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequestDto {
    private String email;
    private int projectId;

    @Builder
    public AdminRequestDto(String email, int projectId) {
        this.email = email;
        this.projectId = projectId;
    }

}
