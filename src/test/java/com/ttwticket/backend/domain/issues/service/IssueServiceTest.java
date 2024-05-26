package com.ttwticket.backend.domain.issues.service;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Priority;
import com.ttwticket.backend.domain.issues.Status;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueRequestDto;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    @Autowired
    IssueService issueService;

    @Autowired
    IssueRepository issueRepository;;

    @BeforeEach
    void setUp() {
        issueRepository.deleteAll();
    }

    @Test
    @DisplayName("이슈 생성 성공")
    void createIssue() {
        //given
        IssueRequestDto issueRequestDto = IssueRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .userId(154)
                .reporter("t_reporter")
                .priority(Priority.major)
                .status(Status.NEW)
                .build();

        //when
        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueRequestDto, 111);

        //then
        Issue issue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

        assertEquals(issueIdResponseDto.getIssueId(), issue.getIssueId());
        assertEquals("t_title", issue.getTitle());
        assertEquals("t_description", issue.getDescription());
        assertEquals(154, issue.getUserId());
        assertEquals("t_reporter", issue.getReporter());
        assertEquals(Priority.major, issue.getPriority());
        assertEquals(Status.NEW, issue.getStatus());
    }

    @Test
    void getAllIssues() {
    }

    @Test
    void getIssue() {
    }

    @Test
    void modifyIssue() {
    }
}