package com.ttwticket.backend.domain.projects.controller;

import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("")
    public ProjectIdResponseDto create(@RequestBody ProjectRequestDto projectRequestDto) {
        return projectService.createProject(projectRequestDto);
    }

    @PatchMapping("{projectId}")
    public Integer modify(@PathVariable("projectId") Integer projectId, @RequestBody ProjectRequestDto projectRequestDto) {
        return projectService.modifyProject(projectId, projectRequestDto);
    }
}
