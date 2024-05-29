package com.ttwticket.backend.domain.admin.controller;

import com.ttwticket.backend.domain.admin.dto.AdminRequestDto;
import com.ttwticket.backend.domain.admin.service.AdminService;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/assign")
    public UserIdResponseDto assignUser(@RequestBody AdminRequestDto adminRequestDto) {
        return adminService.assignUser(adminRequestDto);
    }

    @PostMapping("/project")
    public ProjectIdResponseDto create(@RequestBody ProjectRequestDto projectRequestDto) {
        return adminService.createProject(projectRequestDto);
    }

    @PostMapping("/register")
    public UserIdResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        return adminService.registerUser(userRequestDto);
    }


}
