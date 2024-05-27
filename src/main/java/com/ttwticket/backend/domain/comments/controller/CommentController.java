package com.ttwticket.backend.domain.comments.controller;

import com.ttwticket.backend.domain.comments.dto.*;
import com.ttwticket.backend.domain.comments.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("{projectId}/issues/{issueId}/comments")
    public CommentResponseDto create(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId, @Validated @RequestBody CommentRequestDto commentRequestDto) throws SQLException {
        return commentService.saveComment(commentRequestDto,issueId);
    }

    @GetMapping("{projectId}/issues/{issueId}/comments")
    public List<CommentListDto> getAllCommentsByPostId(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId) throws SQLException {
        return commentService.getAllCommentsByIssueId(issueId);
    }

    @PatchMapping("{projectId}/issues/{issueId}/comments/{commentId}")
    public CommentModifyResponseDto modify(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId, @PathVariable("commentId") Integer commentId, @Validated @RequestBody CommentModifyRequestDto commentModifyRequest) throws SQLException {
        return commentService.modifyComment(commentModifyRequest, issueId, commentId);
    }

    @DeleteMapping("{projectId}/issues/{issueId}/comments/{commentId}")
    public void delete(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId, @PathVariable("commentId") Integer commentId) throws SQLException {
        commentService.deleteComment(issueId, commentId);
    }
}
