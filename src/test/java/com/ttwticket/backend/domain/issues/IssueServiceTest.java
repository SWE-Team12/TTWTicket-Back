package com.ttwticket.backend.domain.issues;

import com.ttwticket.backend.domain.assignees.AssigneeRepository;
import com.ttwticket.backend.domain.assignees.dto.AssigneeRequestDto;
import com.ttwticket.backend.domain.assignees.dto.AssigneeResponseDto;
import com.ttwticket.backend.domain.assignees.service.AssigneeService;
import com.ttwticket.backend.domain.issues.dto.*;
import com.ttwticket.backend.domain.issues.service.IssueService;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.dto.ProjectResponseDto;
import com.ttwticket.backend.domain.projects.service.ProjectService;
import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import com.ttwticket.backend.domain.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    @Autowired
    IssueService issueService;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AssigneeService assigneeService;

    @Autowired
    AssigneeRepository assigneeRepository;


    private ProjectRequestDto projectRequestDto;
    private ProjectIdResponseDto projectIdResponseDto;
    private UserRequestDto userRequestDto;
    private UserIdResponseDto userIdResponseDto;
    private List<UserRequestDto> userRequestDtos;
    private Project project;
    private User user;

    @BeforeEach
    void setUp() {
        assigneeRepository.deleteAll();
        issueRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();

        // 단일 프로젝트 생성
        projectRequestDto = ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build();

        projectIdResponseDto = projectService.createProject(projectRequestDto);
        project = projectRepository.findByProjectId(projectIdResponseDto.getProjectId());

        // 단일 유저 생성
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("t_name")
                .email("test_email")
                .password("t_password")
                .role(Role.Tester)
                .build();

        userIdResponseDto = userService.registerUser(userRequestDto);

        user = userRepository.findByUserIdAndIsDeleted(userIdResponseDto.getUserId(), false);

        // 유저 리스트 생성
        userRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> UserRequestDto.builder()
                        .name("t_name" + (i+1))
                        .email("test_email" + (i+1))
                        .password("t_password" + (i+1))
                        .role(Role.Tester)
                        .build())
                .toList();

    }

    @Test
    @DisplayName("이슈 생성 성공")
    void 이슈생성() {
        //given
        IssueCreateRequestDto issueCreateRequestDto = IssueCreateRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .priority(Priority.major)
                .userId(userIdResponseDto.getUserId())
                .build();

        //when
        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

        //then
        Issue issue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

        assertEquals(issueIdResponseDto.getIssueId(), issue.getIssueId());
        assertEquals("t_title", issue.getTitle());
        assertEquals("t_description", issue.getDescription());
        assertEquals(Priority.major, issue.getPriority());
        assertEquals(userIdResponseDto.getUserId(), issue.getUserId());
    }


    @Test
    @DisplayName("단일 이슈 조회 성공")
    void 단일이슈조회() {
        // given
        IssueCreateRequestDto issueCreateRequestDto = IssueCreateRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .priority(Priority.major)
                .userId(userIdResponseDto.getUserId())
                .build();

        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

        Integer id = issueIdResponseDto.getIssueId();

        // when
        IssueResponseDto issueResponseDto = issueService.getIssue(projectIdResponseDto.getProjectId(), id);

        // then
        assertEquals(id, issueResponseDto.getIssueId());
        assertEquals("t_title", issueResponseDto.getTitle());
        assertEquals("t_description", issueResponseDto.getDescription());
        assertEquals(Priority.major, issueResponseDto.getPriority());
    }

    @Test
    @DisplayName("프로젝트의 모든 이슈 조회 성공")
    void 모든이슈조회() {
        // given
        List<IssueCreateRequestDto> issueCreateRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("t_title" + i)
                        .description("t_description" + i)
                        .priority(Priority.major)
                        .userId(userIdResponseDto.getUserId())
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
            issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        }

        // when
        List<IssueResponseDto> issueResponseDtos = issueService.getProjectIssues(projectIdResponseDto.getProjectId());

        // then
        for (int i=0; i<issueResponseDtos.size(); i++) {
            assertEquals("t_title" + (i+1), issueResponseDtos.get(i).getTitle());
            assertEquals("t_description" + (i+1), issueResponseDtos.get(i).getDescription());
            assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
        }

        assertEquals(issueCreateRequestDtos.size(), issueResponseDtos.size());

    }

    @Test
    @DisplayName("developer에게 할당된 모든 이슈 조회 성공")
    void 할당된이슈조회() {
        // given
        List<IssueCreateRequestDto> issueCreateRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("t_title" + i)
                        .description("t_description" + i)
                        .priority(Priority.major)
                        .userId(userIdResponseDto.getUserId())
                        .build())
                .collect(Collectors.toList());


        for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
            issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        }

        AssigneeRequestDto assigneeRequestDto = new AssigneeRequestDto(user.getEmail());
        List<Issue> issues = issueRepository.findByProject_ProjectId(projectIdResponseDto.getProjectId());

        for(int i=0;i < issues.size();i++)
        {
            assigneeService.assignUser(assigneeRequestDto, issues.get(i).getIssueId());
        }

        // when
        List<IssueResponseDto> issueResponseDtos = issueService.getAssignedIssues(projectIdResponseDto.getProjectId(), user.getUserId());

        // then
        for (int i=0; i<issueResponseDtos.size(); i++) {
            assertEquals("t_title" + (i+1), issueResponseDtos.get(i).getTitle());
            assertEquals("t_description" + (i+1), issueResponseDtos.get(i).getDescription());
            assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
        }
    }



    @Test
    @DisplayName("tester가 report한 모든 이슈 조회 성공")
    void getReportedIssues() {
        // given
        List<IssueCreateRequestDto> issueCreateRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("t_title" + i)
                        .description("t_description" + i)
                        .priority(Priority.major)
                        .userId(userIdResponseDto.getUserId())
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
            issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        }

        // when
        List<IssueResponseDto> issueResponseDtos = issueService.getReportedIssues(projectIdResponseDto.getProjectId(), user.getUserId());

        // then
        for (int i=0; i<issueResponseDtos.size(); i++) {
            assertEquals("t_title" + (i+1), issueResponseDtos.get(i).getTitle());
            assertEquals("t_description" + (i+1), issueResponseDtos.get(i).getDescription());
            assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
        }
    }

    @Test
    @DisplayName("역할 별 이슈 조회 성공")
    void getSearchableIssues() {
        // given
        List<IssueCreateRequestDto> issueCreateRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("t_title" + i)
                        .description("t_description" + i)
                        .priority(Priority.major)
                        .userId(userIdResponseDto.getUserId())
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
            issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        }

        // when
        List<IssueResponseDto> issueResponseDtos = issueService.getSearchableIssues(projectIdResponseDto.getProjectId(), user.getUserId());

        // then
        for (int i=0; i<issueResponseDtos.size(); i++) {
            assertEquals("t_title" + (i+1), issueResponseDtos.get(i).getTitle());
            assertEquals("t_description" + (i+1), issueResponseDtos.get(i).getDescription());
            assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
        }
    }

    @Test
    @DisplayName("이슈 상태 수정 성공")
    void 이슈상태수정() {
        // given
        IssueCreateRequestDto issueCreateRequestDto = IssueCreateRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .priority(Priority.major)
                .userId(userIdResponseDto.getUserId())
                .build();

        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

        // when
        IssueStatusChangeRequestDto issueStatusChangeRequestDto = new IssueStatusChangeRequestDto(Status.NEW);

        int id = issueService.modifyIssue(projectIdResponseDto.getProjectId(), issueIdResponseDto.getIssueId(), issueStatusChangeRequestDto);

        // then
        assertEquals(Status.NEW, issueStatusChangeRequestDto.getStatus());
        assertEquals(issueIdResponseDto.getIssueId(), id);

    }
}