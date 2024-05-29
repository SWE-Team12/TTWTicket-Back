package com.ttwticket.backend.domain.users.dto;

import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer projectId;

    @Builder
    public UserRequestDto(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User toEntity(String password, Project project) {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .project(project)
                .build();
    }
}
