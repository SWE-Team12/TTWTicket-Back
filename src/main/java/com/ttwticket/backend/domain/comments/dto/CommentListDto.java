package com.ttwticket.backend.domain.comments.dto;

import com.ttwticket.backend.domain.comments.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Setter
@AllArgsConstructor
public class CommentListDto {
    private Integer CommentId;

    private String message;

    private String name;

    private Integer issueId;

    private String createdAt;

    public CommentListDto(Comment comment) {
        this.CommentId = comment.getCommentId();
        this.message = comment.getMessage();
        this.name = comment.getUser().getName();
        this.issueId = comment.getIssue().getIssueId();
        this.createdAt = comment.getCreatedAt().toString();
    }
}
