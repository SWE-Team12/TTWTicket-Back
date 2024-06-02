package com.ttwticket.backend.domain.comments;

import com.ttwticket.backend.domain.issues.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findCommentsByIssue(Issue issue);
    public Comment findCommentByIssue(Issue issue);
    public Comment findCommentByCommentId(Integer commentId);
}
