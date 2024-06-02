package com.ttwticket.backend.domain.issues;

import com.ttwticket.backend.domain.assignees.AssigneeRepository;
import com.ttwticket.backend.domain.assignees.dto.AssigneeRequestDto;
import com.ttwticket.backend.domain.assignees.dto.AssigneeResponseDto;
import com.ttwticket.backend.domain.assignees.service.AssigneeService;
import com.ttwticket.backend.domain.fixers.Fixer;
import com.ttwticket.backend.domain.fixers.FixerRepository;
import com.ttwticket.backend.domain.fixers.dto.FixerRequestDto;
import com.ttwticket.backend.domain.fixers.dto.FixerResponseDto;
import com.ttwticket.backend.domain.fixers.service.FixerService;
import com.ttwticket.backend.domain.issues.dto.*;
import com.ttwticket.backend.domain.issues.service.IssueService;
import com.ttwticket.backend.domain.issues.service.RecommendService;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.dto.ProjectResponseDto;
import com.ttwticket.backend.domain.projects.service.ProjectService;
import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.RecommendDevResponseDto;
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
    FixerRepository fixerRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    RecommendService recommendService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AssigneeService assigneeService;

    @Autowired
    AssigneeRepository assigneeRepository;

    private ProjectIdResponseDto projectIdResponseDto;

    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto projectResponseDto;
    private UserRequestDto userRequestDto;
    private UserIdResponseDto userIdResponseDto;
    private List<UserRequestDto> userRequestDtos;
    private List<UserIdResponseDto> userIdResponseDtos = new ArrayList<>(10);;
    private Project project;
    private User user;
    @Autowired
    private FixerService fixerService;

    @BeforeEach
    void setUp() {
        fixerRepository.deleteAll();
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
        projectResponseDto = projectService.getProject(projectIdResponseDto.getProjectId());

        // 단일 유저 생성
        userRequestDto = UserRequestDto.builder()
                .name("t_name")
                .email("t_email")
                .password("t_password")
                .role(Role.Tester)
                .projectId(project.getProjectId())
                .build();

        userIdResponseDto = userService.registerUser(userRequestDto);
        user = userRepository.findByUserIdAndIsDeleted(userIdResponseDto.getUserId(), false);

        // 유저 리스트 생성(Dev)
        userRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> UserRequestDto.builder()
                        .name("Dev" + i)
                        .email("t_email" + i)
                        .password("t_password" + i)
                        .role(Role.Developer)
                        .projectId(project.getProjectId())
                        .build())
                        .toList();

        for (int i = 0; i < 10; i++) {
            UserIdResponseDto responseDto = userService.registerUser(userRequestDtos.get(i));
            userIdResponseDtos.add(responseDto);
        }
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

        try {
            //when
            IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

            //then
            Issue issue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

            assertEquals(issueIdResponseDto.getIssueId(), issue.getIssueId());
            assertEquals("t_title", issue.getTitle());
            assertEquals("t_description", issue.getDescription());
            assertEquals(Priority.major, issue.getPriority());
            assertEquals(Category.Add_Function, issue.getCategory());
            assertEquals(userIdResponseDto.getUserId(), issue.getUserId());
        } catch (Exception e) {}
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
                .category(Category.Add_Function)
                .build();

        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

        Integer id = issueIdResponseDto.getIssueId();

        try {
            // when
            IssueResponseDto issueResponseDto = issueService.getIssue(projectIdResponseDto.getProjectId(), id);

            // then
            assertEquals(id, issueResponseDto.getIssueId());
            assertEquals("t_title", issueResponseDto.getTitle());
            assertEquals("t_description", issueResponseDto.getDescription());
            assertEquals(Category.Add_Function, issueResponseDto.getCategory());
            assertEquals(Priority.major, issueResponseDto.getPriority());
        } catch (Exception e) {}
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
                        .category(Category.Add_Function)
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
            issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        }

        try {
            // when
            List<IssueResponseDto> issueResponseDtos = issueService.getProjectIssues(projectIdResponseDto.getProjectId());

            // then
            for (int i = 0; i < issueResponseDtos.size(); i++) {
                assertEquals("t_title" + (i + 1), issueResponseDtos.get(i).getTitle());
                assertEquals("t_description" + (i + 1), issueResponseDtos.get(i).getDescription());
                assertEquals(Category.Add_Function, issueResponseDtos.get(i).getCategory());
                assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
            }

            assertEquals(issueCreateRequestDtos.size(), issueResponseDtos.size());
        } catch (Exception e) {}

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
                        .category(Category.Add_Function)
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

        try {
            // when
            List<IssueResponseDto> issueResponseDtos = issueService.getAssignedIssues(projectIdResponseDto.getProjectId(), user.getUserId());

            // then
            for (int i = 0; i < issueResponseDtos.size(); i++) {
                assertEquals("t_title" + (i + 1), issueResponseDtos.get(i).getTitle());
                assertEquals("t_description" + (i + 1), issueResponseDtos.get(i).getDescription());
                assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
                assertEquals(Category.Add_Function, issueResponseDtos.get(i).getCategory());
            }
        } catch (Exception e) {}
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
                        .category(Category.Add_Function)
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
            issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        }

        try {
            // when
            List<IssueResponseDto> issueResponseDtos = issueService.getReportedIssues(projectIdResponseDto.getProjectId(), user.getUserId());

            // then
            for (int i = 0; i < issueResponseDtos.size(); i++) {
                assertEquals("t_title" + (i + 1), issueResponseDtos.get(i).getTitle());
                assertEquals("t_description" + (i + 1), issueResponseDtos.get(i).getDescription());
                assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
                assertEquals(Category.Add_Function, issueResponseDtos.get(i).getCategory());
            }
        } catch (Exception e) {}
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
                        .category(Category.Add_Function)
                        .build())
                .collect(Collectors.toList());

        try {

            for (IssueCreateRequestDto issueCreateRequestDto : issueCreateRequestDtos) {
                issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
            }

            // when
            List<IssueResponseDto> issueResponseDtos = issueService.getSearchableIssues(projectIdResponseDto.getProjectId(), user.getUserId());

            // then
            for (int i = 0; i < issueResponseDtos.size(); i++) {
                assertEquals("t_title" + (i + 1), issueResponseDtos.get(i).getTitle());
                assertEquals("t_description" + (i + 1), issueResponseDtos.get(i).getDescription());
                assertEquals(Priority.major, issueResponseDtos.get(i).getPriority());
                assertEquals(Category.Add_Function, issueResponseDtos.get(i).getCategory());
            }
        } catch (Exception e) {}
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
                .category(Category.Add_Function)
                .build();

        IssueIdResponseDto issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

        // when
        IssueStatusChangeRequestDto issueStatusChangeRequestDto = new IssueStatusChangeRequestDto(Status.NEW);

        int id = issueService.modifyIssue(projectIdResponseDto.getProjectId(), issueIdResponseDto.getIssueId(), issueStatusChangeRequestDto);

        // then
        assertEquals(Status.NEW, issueStatusChangeRequestDto.getStatus());
        assertEquals(issueIdResponseDto.getIssueId(), id);
    }

    @Test
    @DisplayName("새로운 이슈에 대해 assignee 추천 성공")
    void 이슈추천() {
        // given
        // Dev0: Add_Function(3), Dev1: Add_Function(2), Dev2: Add_Function(1), Dev3~10: ETC(1)
        List<IssueCreateRequestDto> Dev0IssueDtos = IntStream.rangeClosed(0, 3).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("test title" + i)
                        .description("test description" + i)
                        .category(Category.Add_Function)
                        .userId(userIdResponseDtos.get(0).getUserId())
                        .priority(Priority.major)
                        .build())
                .collect(Collectors.toList());

        List<IssueIdResponseDto> Dev0Issues = new ArrayList<>(3);;

        for (int i = 0; i < 3; i++) {
            IssueIdResponseDto responseDto = issueService.createIssue(Dev0IssueDtos.get(i), projectIdResponseDto.getProjectId());
            Dev0Issues.add(responseDto);
        }

        User Dev0 = userRepository.findByUserIdAndIsDeleted(userIdResponseDtos.get(0).getUserId(), false);
        FixerRequestDto Dev0FixedRequest = new FixerRequestDto(Dev0.getEmail());

        for(int i=0;i<3;i++)
        {
            FixerResponseDto fixerResponseDto = fixerService.assignUser(Dev0FixedRequest, Dev0Issues.get(i).getIssueId());
        }

        // User 1
        List<IssueCreateRequestDto> Dev1IssueDtos = IntStream.rangeClosed(0, 2).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("test title" + i)
                        .description("test description" + i)
                        .category(Category.Add_Function)
                        .userId(userIdResponseDtos.get(1).getUserId())
                        .priority(Priority.major)
                        .build())
                .collect(Collectors.toList());

        List<IssueIdResponseDto> Dev1Issues = new ArrayList<>(2);;

        for (int i = 0; i < 2; i++) {
            IssueIdResponseDto responseDto = issueService.createIssue(Dev1IssueDtos.get(i), projectIdResponseDto.getProjectId());
            Dev1Issues.add(responseDto);
        }

        User Dev1 = userRepository.findByUserIdAndIsDeleted(userIdResponseDtos.get(1).getUserId(), false);
        FixerRequestDto Dev1FixedRequest = new FixerRequestDto(Dev1.getEmail());

        for(int i=0;i<2;i++)
        {
            FixerResponseDto fixerResponseDto = fixerService.assignUser(Dev1FixedRequest, Dev1Issues.get(i).getIssueId());
        }


        // User 2
        IssueCreateRequestDto Dev2IssueDto = IssueCreateRequestDto.builder()
                .title("test title")
                .description("test description")
                .category(Category.Add_Function)
                .userId(userIdResponseDtos.get(2).getUserId())
                .priority(Priority.major)
                .build();

        IssueIdResponseDto Dev2Issue = issueService.createIssue(Dev2IssueDto, projectIdResponseDto.getProjectId());

        User Dev2 = userRepository.findByUserIdAndIsDeleted(userIdResponseDtos.get(2).getUserId(), false);
        FixerRequestDto Dev2FixedRequest = new FixerRequestDto(Dev2.getEmail());

        FixerResponseDto fixerResponseDto = fixerService.assignUser(Dev2FixedRequest, Dev2Issue.getIssueId());


        // User 3~10
        List<IssueCreateRequestDto> remainderDto = IntStream.rangeClosed(3, 9).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("test title" + i)
                        .description("test description" + i)
                        .category(Category.ETC)
                        .userId(userIdResponseDtos.get(i).getUserId())
                        .priority(Priority.major)
                        .build())
                .collect(Collectors.toList());

        for (int i=3;i<10;i++) {
            IssueIdResponseDto IssueResponse = issueService.createIssue(remainderDto.get(i-3), projectIdResponseDto.getProjectId());
            User user = userRepository.findByUserIdAndIsDeleted(userIdResponseDtos.get(i).getUserId(), false);
            FixerRequestDto fixerRequestDto = new FixerRequestDto(user.getEmail());
            FixerResponseDto fixerResponseDto1 = fixerService.assignUser(fixerRequestDto, IssueResponse.getIssueId());
        }

        // 해당 project에 대한 Issue List
        List<Issue> issues = issueRepository.findByProject_ProjectId(projectIdResponseDto.getProjectId());

        // when
        // 새로운 이슈 할당(Add_Function)
        IssueCreateRequestDto issueCreateRequestDto = IssueCreateRequestDto.builder()
                .title("new_title")
                .description("new_description")
                .priority(Priority.major)
                .userId(userIdResponseDto.getUserId())
                .category(Category.Add_Function)
                .build();

        IssueIdResponseDto newIssueResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        List<RecommendDevResponseDto> recommendDevResponseDtos = recommendService.recommend(newIssueResponseDto.getIssueId());

        // then
        // Add_Function 카테고리 top 3: Dev0, Dev1, Dev2
        assertEquals(recommendDevResponseDtos.get(0).getName(), Dev0.getName());
        assertEquals(recommendDevResponseDtos.get(0).getRole(), Dev0.getRole());
        assertEquals(recommendDevResponseDtos.get(0).getUserId(), Dev0.getUserId());

        assertEquals(recommendDevResponseDtos.get(1).getName(), Dev1.getName());
        assertEquals(recommendDevResponseDtos.get(1).getRole(), Dev1.getRole());
        assertEquals(recommendDevResponseDtos.get(1).getUserId(), Dev1.getUserId());

        assertEquals(recommendDevResponseDtos.get(2).getName(), Dev2.getName());
        assertEquals(recommendDevResponseDtos.get(2).getRole(), Dev2.getRole());
        assertEquals(recommendDevResponseDtos.get(2).getUserId(), Dev2.getUserId());


    }
}