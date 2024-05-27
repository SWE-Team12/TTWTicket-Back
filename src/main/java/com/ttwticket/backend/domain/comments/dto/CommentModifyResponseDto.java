package com.ttwticket.backend.domain.comments.dto;

import com.ttwticket.backend.domain.comments.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentModifyResponseDto {
    private Integer commentId;
    private String message;
    private String name;
    private Integer issueId;
    private String createdAt;
    private String updatedAt;

    public CommentModifyResponseDto(Comment comment, Integer issueId) {
        this.commentId = comment.getCommentId();
        this.message = comment.getMessage();
        this.name = comment.getUser().getName();
        this.issueId = issueId;
        this.createdAt = comment.getCreatedAt().toString();
        this.updatedAt = comment.getUpdatedAt().toString();
    }
}
