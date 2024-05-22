package com.ttwticket.backend.domain.users.dto;

import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private Role role;

    @Builder
    public UserRequestDto(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}
