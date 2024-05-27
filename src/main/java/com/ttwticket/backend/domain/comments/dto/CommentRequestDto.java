package com.ttwticket.backend.domain.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private String message;
    private Integer userId;
}
