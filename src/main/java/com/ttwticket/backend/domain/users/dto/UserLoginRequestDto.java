package com.ttwticket.backend.domain.users.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginRequestDto {
    private String email;
    private String password;
}
