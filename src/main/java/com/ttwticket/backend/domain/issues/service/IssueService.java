package com.ttwticket.backend.domain.issues.service;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Status;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueRequestDto;
import com.ttwticket.backend.domain.issues.dto.IssueResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    @Transactional
    public IssueIdResponseDto createIssue(IssueRequestDto issueRequestDto) {
        IssueIdResponseDto issueIdResponseDto = IssueIdResponseDto.builder()
                .issueId(issueRepository.save(issueRequestDto.toEntity()).getIssueId())
                .build();
        return issueIdResponseDto;
    }

    @Transactional
    public Integer modifyIssue(Integer issueId, IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        issueRepository.findByIssueId(issueId).modifyIssue(issueStatusChangeRequestDto);
        return issueId;
    }

    public List<IssueResponseDto> getAllIssues(Integer projectId) {
        List<Issue> issues = issueRepository.findByProjectId(projectId);
        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }

    public List<IssueResponseDto> getIssuesByStatus(Integer projectId, Status status) {
        List<Issue> issues = issueRepository.findByProjectIdAndStatus(projectId, status);
        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }


    public List<IssueResponseDto> getIssuesByUser(Integer projectId, User user) {
        List<Issue> issues = issueRepository.findByProjectIdAndFixer(projectId, user);
        return issues.stream()
                .map(issue -> IssueResponseDto.builder().issue(issue).build())
                .collect(Collectors.toList());
    }


}
