package com.ttwticket.backend.domain.issues;

import com.ttwticket.backend.domain.BaseTimeEntity;
import com.ttwticket.backend.domain.assignee.Assignee;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.users.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Issue extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int issueId;

    @Column
    private int userId;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String reporter;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean isOpened;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder
    public Issue(String title, String description, String reporter, Status status, Priority priority, Integer userId, Project project) {
        this.title = title;
        this.description = description;
        this.reporter = reporter;
        this.status = status;
        this.priority = priority;
        this.isOpened = true;
        this.userId = userId;
        this.project = project;
    }

    public void modifyIssue(IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        this.status = issueStatusChangeRequestDto.getStatus();
    }
}
