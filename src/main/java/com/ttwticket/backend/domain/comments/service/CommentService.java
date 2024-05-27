package com.ttwticket.backend.domain.comments.service;

import com.ttwticket.backend.domain.comments.Comment;
import com.ttwticket.backend.domain.comments.CommentRepository;
import com.ttwticket.backend.domain.comments.dto.*;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto saveComment(CommentRequestDto commentRequest, Integer issueId) throws SQLException {
        User user = userValid(commentRequest.getUserId());
        Issue issue = issueValid(issueId);

        Comment comment = Comment.createComment(commentRequest.getMessage(), user, issue);
        commentRepository.save(comment);

        return new CommentResponseDto(comment, user.getName(), issueId);
    }

    public List<CommentListDto> getAllCommentsByIssueId(Integer issueId) throws SQLException {
        Issue issue = issueValid(issueId);
        return commentRepository.findCommentsByIssue(issue).stream().map(CommentListDto::new).collect(Collectors.toList());
    }

    @Transactional
    public CommentModifyResponseDto modifyComment(CommentModifyRequestDto commentModifyRequest, Integer issueId, Integer commentId) throws SQLException {
        issueValid(issueId);
        Comment comment = commentRepository.findCommentByCommentId(commentId);

        comment.modifyComment(commentModifyRequest.getMessage());

        return new CommentModifyResponseDto(commentRepository.saveAndFlush(comment), issueId);
    }

    @Transactional
    public void deleteComment(Integer issueId, Integer commentId) throws SQLException {
        issueValid(issueId);
        Comment comment = commentRepository.findCommentByCommentId(commentId);

        commentRepository.delete(comment);
    }

    public User userValid(Integer userId) {
        return userRepository.findByUserIdAndIsDeleted(userId, false);
    }

    public Issue issueValid(Integer issueId) {
        return issueRepository.findByIssueId(issueId);
    }

}
