package com.ttwticket.backend.domain.users.controller;

import com.ttwticket.backend.domain.users.dto.RecommendDevResponseDto;
import com.ttwticket.backend.domain.users.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Slf4j
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("{projectId}/recommend")
    public List<RecommendDevResponseDto> recommendDeveloper(@PathVariable("projectId") Integer projectId) {
        return recommendService.recommend();
    }
}
