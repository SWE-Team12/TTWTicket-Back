package com.ttwticket.backend.domain.fixer.controller;

import com.ttwticket.backend.domain.fixer.dto.FixerRequestDto;
import com.ttwticket.backend.domain.fixer.dto.FixerResponseDto;
import com.ttwticket.backend.domain.fixer.service.FixerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class FixerController {
    private final FixerService fixerService;

    @PostMapping("{projectId}/issues/{issueId}/fixers")
    public FixerResponseDto assign(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId, @Validated @RequestBody FixerRequestDto fixerRequestDto) throws SQLException {
        return fixerService.assignUser(fixerRequestDto, issueId);
    }

    @GetMapping("{projectId}/issues/{issueId}/fixers")
    public List<FixerResponseDto> getAll(@PathVariable("projectId") Integer projectId, @PathVariable("issueId") Integer issueId) throws SQLException {
        return fixerService.getUsers(issueId);
    }
}
