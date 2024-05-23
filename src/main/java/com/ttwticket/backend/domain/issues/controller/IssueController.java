package com.ttwticket.backend.domain.issues.controller;

import com.ttwticket.backend.domain.issues.Status;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueRequestDto;
import com.ttwticket.backend.domain.issues.dto.IssueResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.issues.service.IssueService;
import com.ttwticket.backend.domain.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/project/{projectId}/issues")
@RequiredArgsConstructor
@Slf4j
public class IssueController {

    private final IssueService issueService;

    @PostMapping("")
    public IssueIdResponseDto create(@RequestBody IssueRequestDto issueRequestDto) {
        return issueService.createIssue(issueRequestDto);
    }

    @GetMapping("")
    public List<IssueResponseDto> getAllIssues(@PathVariable("projectId") Integer projectId) {
        return issueService.getAllIssues(projectId);
    }

    @GetMapping("/search")
    public List<IssueResponseDto> searchIssues(@PathVariable("projectId") Integer projectId,
                                               @RequestParam(value = "status", required = false) Status status,
                                               @RequestParam(value = "fixer", required = false) User fixer,
                                               @RequestParam(value = "reporter", required = false) User reporter) {
        if (status != null) {
            return issueService.getIssuesByStatus(projectId, status);
        } else if (fixer != null) {
            return issueService.getIssuesByUser(projectId, fixer);
        } else if (reporter != null) {
            return issueService.getIssuesByUser(projectId, reporter);
        } else {
            // Handle case when no query parameter is provided
            // You can return an error response or handle it as per your application's requirements
            return Collections.emptyList();
        }
    }

    @PatchMapping("{issueId}")
    public Integer modify(@PathVariable("issueId") Integer issueId, @RequestBody IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        return issueService.modifyIssue(issueId, issueStatusChangeRequestDto);
    }


}
