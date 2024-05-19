package com.ttwticket.backend.domain.projects.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    @PostMapping("")
    public void createProject() {
    }

    @GetMapping("/{projectId}")
    public void getProjects() {
    }

    @PatchMapping("/{projectId}")
    public void modifyProject() {
    }
}
