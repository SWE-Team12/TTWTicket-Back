package com.ttwticket.backend.domain.issues.service;

import com.ttwticket.backend.domain.assignees.Assignee;
import com.ttwticket.backend.domain.assignees.AssigneeRepository;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.dto.*;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AssigneeRepository assigneeRepository;

    @Transactional
    public IssueIdResponseDto createIssue(IssueCreateRequestDto issueCreateRequestDto, Integer projectId) {
        Project project = projectValid(projectId);
        String reporter = userRepository.findByUserIdAndIsDeleted(issueCreateRequestDto.getUserId(),false).getName();
        IssueIdResponseDto issueIdResponseDto = IssueIdResponseDto.builder()
                .issueId(issueRepository.save(issueCreateRequestDto.toEntity(project, reporter)).getIssueId())
                .build();
        return issueIdResponseDto;
    }

    public IssueResponseDto getIssue(Integer projectId, Integer issueId) {
        return IssueResponseDto.builder()
                .issue(issueRepository.findByProject_ProjectIdAndIssueId(projectId, issueId))
                .build();
    }

    public List<IssueResponseDto> getProjectIssues(Integer projectId) {
        List<Issue> issues = issueRepository.findByProject_ProjectId(projectId);
        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }

    public List<IssueResponseDto> getAssignedIssues(Integer projectId, Integer userId) {
        List<Issue> issues = issueRepository.findByProject_ProjectId(projectId);
        List<Assignee> assignees = assigneeRepository.findAssigneeByUser(userRepository.findByUserIdAndIsDeleted(userId, false));
        List<Integer> issueIds = assignees.stream()
                        .map(Assignee::getIssue)
                        .map(Issue::getIssueId).collect(Collectors.toList());

        List<Issue> filteredIssue = issues.stream()
                .filter(issue->issueIds.contains(issue.getIssueId()))
                .collect(Collectors.toList());

        return filteredIssue.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }

    public List<IssueResponseDto> getReportedIssues(Integer projectId, Integer userId) {
        List<Issue> issues = issueRepository.findByProject_ProjectIdAndUserId(projectId, userId);

        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }

    public List<IssueResponseDto> getSearchableIssues(Integer projectId, Integer userId) {
        Role role = userRepository.findByUserIdAndIsDeleted(userId, false).getRole();

        List<IssueResponseDto> issues = null;

        switch (role) {
            case Admin:
                issues = getProjectIssues(projectId);
                break;

            case PL:
                issues = getProjectIssues(projectId);
                break;

            case Developer:
                issues = getAssignedIssues(projectId, userId);
                break;

            case Tester:
                issues = getReportedIssues(projectId, userId);
                break;
        }
        return issues;
    }


    @Transactional
    public Integer modifyIssue(Integer projectId, Integer issueId, IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        issueRepository.findByProject_ProjectIdAndIssueId(projectId, issueId).modifyIssue(issueStatusChangeRequestDto);
        return issueId;
    }

    public Project projectValid(Integer projectId) {
        return projectRepository.findByProjectId(projectId);
    }

}

