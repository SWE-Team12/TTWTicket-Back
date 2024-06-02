package com.ttwticket.backend.domain.assignees;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.users.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Assignee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int assigneeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Builder
    public Assignee(User user, Issue issue) {
        this.user = user;
        this.issue = issue;
    }
}
