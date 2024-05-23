package com.ttwticket.backend.domain.issues;

import com.ttwticket.backend.domain.BaseTimeEntity;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.users.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Issue extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int issueId;

    private int projectId;

    private int userId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private Date reportedDate;

    @Column(nullable = false, length = 20)
    private Status status;

    @Column(nullable = false, length = 20)
    private Priority priority;

    private boolean isOpened;

    @ManyToOne
    @JoinColumn(name = "reporter", referencedColumnName = "name")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "assignee", referencedColumnName = "name")
    private User[] assignee;

    @ManyToOne
    @JoinColumn(name = "fixer", referencedColumnName = "name")
    private User[] fixer;

    @Builder
    public Issue(String title, String description, Status status, Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public void modifyIssue(IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        this.status = issueStatusChangeRequestDto.getStatus();
    }
}
