package com.ttwticket.backend.domain.comments;

import com.ttwticket.backend.domain.BaseTimeEntity;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    public Comment(String message, User user, Issue issue) {
        this.message = message;
        this.user = user;
        this.issue = issue;
    }

    public static Comment createComment(String message, User user, Issue issue) {
        return new Comment(message, user, issue);
    }

    public void modifyComment(String newMessage) {
        this.message = newMessage;
    }

}
