package com.ttwticket.backend.domain.issues.controller;

import com.ttwticket.backend.domain.issues.dto.*;
import com.ttwticket.backend.domain.issues.service.IssueService;
import com.ttwticket.backend.domain.users.dto.RecommendDevResponseDto;
import com.ttwticket.backend.domain.issues.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class IssueController {

    private final IssueService issueService;
    private final RecommendService recommendService;

    @PostMapping("/{projectId}/issues")
    public IssueIdResponseDto create(@PathVariable("projectId") Integer projectId, @Validated @RequestBody IssueCreateRequestDto issueCreateRequestDto) {
        return issueService.createIssue(issueCreateRequestDto, projectId);
    }

    @GetMapping("/{projectId}/issues")
    public List<IssueResponseDto> getAllIssues(@PathVariable("projectId") Integer projectId) {
        return issueService.getProjectIssues(projectId);
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public IssueResponseDto getIssue(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId) {
        return issueService.getIssue(projectId, issueId);
    }

    @GetMapping("/{projectId}/issues/search/{userId}")
    public List<IssueResponseDto> search(@PathVariable("projectId") Integer projectId, @PathVariable("userId") Integer userId) {
        return issueService.getSearchableIssues(projectId, userId);
    }

    @GetMapping("{projectId}/issues/{issueId}/recommend")
    public List<RecommendDevResponseDto> recommendDeveloper(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId) {
        return recommendService.recommend(issueId);
    }

    @PatchMapping("/{projectId}/issues/{issueId}")
    public Integer modify(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId, @RequestBody IssueStatusChangeRequestDto issueStatusChangeRequestDto) {
        return issueService.modifyIssue(projectId, issueId, issueStatusChangeRequestDto);
    }



//
//    @GetMapping("/{projectId}/issues/search")
//    public List<IssueResponseDto> searchIssues(@PathVariable("projectId") Integer projectId,
//                                               @RequestParam(value = "status", required = false) Status status,
//                                               @RequestParam(value = "fixer", required = false) User fixer,
//                                               @RequestParam(value = "reporter", required = false) User reporter) {
//        if (status != null) {
//            return issueService.getIssuesByStatus(projectId, status);
//        } else if (fixer != null) {
//            return issueService.getIssuesByUser(projectId, fixer);
//        } else if (reporter != null) {
//            return issueService.getIssuesByUser(projectId, reporter);
//        } else {
//            // Handle case when no query parameter is provided
//            // You can return an error response or handle it as per your application's requirements
//            return Collections.emptyList();
//        }
//    }
//



}

