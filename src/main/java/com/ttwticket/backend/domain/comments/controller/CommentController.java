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

    
}
