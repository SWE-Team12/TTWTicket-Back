package com.ttwticket.backend.domain.users.dto;

import com.ttwticket.backend.domain.users.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendDevResponseDto {
    private Integer userId;
    private String name;
    private Role role;

    public RecommendDevResponseDto(Integer userId, String name, Role role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
}