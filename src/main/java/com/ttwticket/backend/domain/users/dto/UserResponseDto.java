package com.ttwticket.backend.domain.users.dto;

import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Integer userId;
    private String name;
    private String email;
    private Role role;
    private Integer projectId;

    @Builder
    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.projectId = user.getProject().getProjectId();
    }
}
