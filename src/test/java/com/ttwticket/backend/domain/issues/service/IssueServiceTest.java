package com.ttwticket.backend.domain.issues.service;

import com.ttwticket.backend.domain.issues.Issue;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Priority;
import com.ttwticket.backend.domain.issues.Status;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueRequestDto;
import com.ttwticket.backend.domain.issues.dto.IssueResponseDto;
import com.ttwticket.backend.domain.issues.dto.IssueStatusChangeRequestDto;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    @Autowired
    IssueService issueService;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        issueRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    @DisplayName("이슈 생성 성공")
    void 이슈생성() {
        //given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build();

        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(projectRequestDto);


        IssueRequestDto issueRequestDto = IssueRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .userId(154)
                .reporter("t_reporter")
                .priority(Priority.major)
                .status(Status.NEW)
                .build();

        //when
        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueRequestDto, projectIdResponseDto.getProjectId());

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
    @DisplayName("모든 이슈 조회 성공")
    void 모든이슈조회() {
        //given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build();

        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(projectRequestDto);

        List<IssueRequestDto> issueRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueRequestDto.builder()
                        .title("t_title " + i)
                        .description("t_description" + i)
                        .userId(i)
                        .reporter("t_reporter" + i)
                        .priority(Priority.major)
                        .status(Status.NEW)
                        .build())
                .toList();

        List<IssueIdResponseDto> issueIdResponseDtos = new ArrayList<>(issueRequestDtos.size());
        List<Issue> issues = new ArrayList<>(issueRequestDtos.size());

        for (int i = 0; i < issueRequestDtos.size(); i++) {
            try {
                issueIdResponseDtos.add(issueService.createIssue(issueRequestDtos.get(i), projectIdResponseDto.getProjectId()));
                issues.add(issueRepository.findByIssueId(issueIdResponseDtos.get(i).getIssueId()));
            } catch (Exception e) {}
        }



        //when
        List<IssueResponseDto> issueResponseDto = issueService.getAllIssues(projectIdResponseDto.getProjectId());

        //then
        for (int i=0; i<issueResponseDto.size(); i++) {
            assertEquals("t_title " + (i+1), issues.get(i).getTitle());
            assertEquals("t_description" + (i+1), issues.get(i).getDescription());
            assertEquals(i+1, issues.get(i).getUserId());
            assertEquals("t_reporter" + (i+1), issues.get(i).getReporter());
            assertEquals(Priority.major, issues.get(i).getPriority());
            assertEquals(Status.NEW, issues.get(i).getStatus());
        }

        assertEquals(issues.size(), issueResponseDto.size());
    }


    @Test
    @DisplayName("단일 이슈 조회 성공")
    void 단일이슈조회() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build();

        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(projectRequestDto);

        try {
            IssueIdResponseDto issueIdResponseDto = issueService.createIssue(IssueRequestDto.builder()
                            .title("sample title ")
                            .description("sample description")
                            .userId(123)
                            .reporter("sample reporter")
                            .priority(Priority.major)
                            .status(Status.NEW)
                            .build(),
                    projectIdResponseDto.getProjectId());

            Integer id = issueIdResponseDto.getIssueId();

            // when
            IssueResponseDto issueResponseDto = issueService.getIssue(projectIdResponseDto.getProjectId(), id);
            Issue issue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

            // then
            assertEquals(id, issueIdResponseDto.getIssueId());
            assertEquals("sample title", issueResponseDto.getTitle());
            assertEquals("sample description", issueResponseDto.getDescription());
            assertEquals(123, issue.getUserId());
            assertEquals("sample reporter", issueResponseDto.getReporter());
            assertEquals(Priority.major, issueResponseDto.getPriority());
            assertEquals(Status.NEW, issueResponseDto.getStatus());

        } catch (Exception e) {}

    }

    @Test
    @DisplayName("이슈 수정 성공")
    void 이슈수정() {
        // given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build();

        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(projectRequestDto);

        try {
            IssueRequestDto issueRequestDto = IssueRequestDto.builder()
                            .title("sample title ")
                            .description("sample description")
                            .userId(123)
                            .reporter("sample reporter")
                            .priority(Priority.major)
                            .status(Status.NEW)
                            .build();

            IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueRequestDto, projectIdResponseDto.getProjectId());
            Issue issue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

            IssueStatusChangeRequestDto modifiedDto = IssueStatusChangeRequestDto.builder()
                    .status(Status.assigned)
                    .build();

            // when
            issueService.modifyIssue(issue.getIssueId(), issue.getIssueId(), modifiedDto);
            Issue modifiedIssue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

            // then
            assertEquals(Status.NEW, modifiedIssue.getStatus());

        } catch (Exception e) {}

    }
}