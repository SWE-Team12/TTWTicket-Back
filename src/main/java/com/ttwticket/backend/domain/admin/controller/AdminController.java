package com.ttwticket.backend.domain.admin.controller;

import com.ttwticket.backend.domain.admin.dto.AdminRequestDto;
import com.ttwticket.backend.domain.admin.service.AdminService;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/userassign")
    public UserIdResponseDto assignUser(@RequestBody AdminRequestDto adminRequestDto) {
        return adminService.assignUser(adminRequestDto);
    }

}