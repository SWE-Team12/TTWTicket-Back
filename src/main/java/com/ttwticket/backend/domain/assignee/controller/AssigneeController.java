package com.ttwticket.backend.domain.assignee.controller;

import com.ttwticket.backend.domain.assignee.dto.AssigneeRequestDto;
import com.ttwticket.backend.domain.assignee.dto.AssigneeResponseDto;
import com.ttwticket.backend.domain.assignee.service.AssigneeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class AssigneeController {
    private final AssigneeService assigneeService;

    @PostMapping("{projectId}/issues/{issueId}/assignees")
    public AssigneeResponseDto assign(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId, @Validated @RequestBody AssigneeRequestDto assigneeRequestDto) throws SQLException {
        return assigneeService.assignUser(assigneeRequestDto, issueId);
    }

    @GetMapping("{projectId}/issues/{issueId}/assignees")
    public List<AssigneeResponseDto> getAll(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId) throws SQLException {
        return assigneeService.getUsers(issueId);
    }
}
