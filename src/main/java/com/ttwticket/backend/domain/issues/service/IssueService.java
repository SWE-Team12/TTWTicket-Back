package com.ttwticket.backend.domain.issues.service;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Status;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueRequestDto;
import com.ttwticket.backend.domain.issues.dto.IssueResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.users.User;
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

    @Transactional
    public IssueIdResponseDto createIssue(IssueRequestDto issueRequestDto, Integer projectId) {
        Project project = projectValid(projectId);
        String reporter = userRepository.findByUserIdAndIsDeleted(issueRequestDto.getUserId(),false).getName();
        IssueIdResponseDto issueIdResponseDto = IssueIdResponseDto.builder()
                .issueId(issueRepository.save(issueRequestDto.toEntity(reporter, project)).getIssueId())
                .build();
        return issueIdResponseDto;
    }

    public List<IssueResponseDto> getAllIssues(Integer projectId) {
        List<Issue> issues = issueRepository.findByProject_ProjectId(projectId);
        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }

    public IssueResponseDto getIssue(Integer projectId, Integer issueId) {
        return IssueResponseDto.builder()
                .issue(issueRepository.findByProject_ProjectIdAndIssueId(projectId, issueId))
                .build();
    }

    @Transactional
    public Integer modifyIssue(Integer projectId, Integer issueId, IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        issueRepository.findByProject_ProjectIdAndIssueId(projectId, issueId).modifyIssue(issueStatusChangeRequestDto);
        return issueId;
    }


    public List<IssueResponseDto> getIssuesByStatus(Integer projectId, Status status) {
        List<Issue> issues = issueRepository.findByProject_ProjectIdAndStatus(projectId, status);
        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }

//    public List<IssueResponseDto> getIssuesByUser(Integer projectId, User user) {
//        List<Issue> issues = issueRepository.findByProjectIdAndFixer(projectId, user);
//        return issues.stream()
//                .map(issue -> IssueResponseDto.builder().issue(issue).build())
//                .collect(Collectors.toList());
//    }

    public Project projectValid(Integer projectId) {
        return projectRepository.findByProjectId(projectId);
    }


}
