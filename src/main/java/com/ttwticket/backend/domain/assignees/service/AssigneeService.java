package com.ttwticket.backend.domain.assignees.service;


import com.ttwticket.backend.domain.assignees.Assignee;
import com.ttwticket.backend.domain.assignees.AssigneeRepository;
import com.ttwticket.backend.domain.assignees.dto.AssigneeRequestDto;
import com.ttwticket.backend.domain.assignees.dto.AssigneeResponseDto;
import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Status;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.issues.service.IssueService;
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
public class AssigneeService {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final AssigneeRepository assigneeRepository;
    private final IssueService issueService;

    @Transactional
    public AssigneeResponseDto assignUser(AssigneeRequestDto assigneeRequestDto, Integer issueId) {
        User user = userValid(assigneeRequestDto.getEmail());
        Issue issue = issueValid(issueId);

        IssueStatusChangeRequestDto issueStatusChangeRequestDto = IssueStatusChangeRequestDto.builder()
                .status(Status.assigned)
                .build();

        issueService.modifyIssue(issue.getProject().getProjectId(), issue.getIssueId(), issueStatusChangeRequestDto);

        AssigneeResponseDto assigneeResponseDto = AssigneeResponseDto.builder()
                .assignee(assigneeRepository.save(new Assignee(user, issue)))
                .build();
        return assigneeResponseDto;
    }

    @Transactional
    public List<AssigneeResponseDto> getUsers(Integer issueId) throws SQLException {
        Issue issue = issueValid(issueId);
        List<Assignee> assignees = assigneeRepository.findAssigneeByIssue(issue);
        return assignees.stream()
                .map(assignee -> AssigneeResponseDto.builder().assignee(assignee).build())
                .collect(Collectors.toList());
    }


    public Issue issueValid(Integer issueId) {
        return issueRepository.findByIssueId(issueId);
    }

    public User userValid(String email) {
        return userRepository.findByEmailAndIsDeleted(email, false);
    }

}
