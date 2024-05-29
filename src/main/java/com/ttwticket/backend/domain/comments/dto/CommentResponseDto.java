package com.ttwticket.backend.domain.comments.dto;

import com.ttwticket.backend.domain.comments.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Integer commentId;
    private String message;
    private String name;
    private Integer issueId;
    private String createdAt;

    public CommentResponseDto(Comment comment, String name, Integer issueId) {
        this.commentId = comment.getCommentId();
        this.message = comment.getMessage();
        this.name = name;
        this.issueId = issueId;
        this.createdAt = comment.getCreatedAt().toString();
    }
}
