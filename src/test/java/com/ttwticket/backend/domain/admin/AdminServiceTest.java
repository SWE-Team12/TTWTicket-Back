package com.ttwticket.backend.domain.admin;

import com.ttwticket.backend.domain.admin.dto.AdminRequestDto;
import com.ttwticket.backend.domain.admin.service.AdminService;
import com.ttwticket.backend.domain.assignees.AssigneeRepository;
import com.ttwticket.backend.domain.assignees.dto.AssigneeRequestDto;
import com.ttwticket.backend.domain.assignees.dto.AssigneeResponseDto;
import com.ttwticket.backend.domain.assignees.service.AssigneeService;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Priority;
import com.ttwticket.backend.domain.issues.dto.IssueCreateRequestDto;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private AssigneeService assigneeService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private AdminService adminService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssigneeRepository assigneeRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectRepository projectRepository;

    private UserRequestDto userRequestDto;
    private UserIdResponseDto userIdResponseDto;
    private IssueIdResponseDto issueIdResponseDto;
    private ProjectRequestDto projectRequestDto;
    private ProjectIdResponseDto projectIdResponseDto;
    private List<UserRequestDto> userRequestDtos;
    private List<IssueCreateRequestDto> issueCreateRequestDtos;
    private List<IssueIdResponseDto> issueIdResponseDtos = new ArrayList<>();;
    private User user;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

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

        // 단일 유저 생성
        userRequestDto = UserRequestDto.builder()
                .name("t_name")
                .email("test_email")
                .password("t_password")
                .role(Role.Developer)
                .build();

        userIdResponseDto = userService.registerUser(userRequestDto);
        user = userRepository.findByUserIdAndIsDeleted(userIdResponseDto.getUserId(), false);

        // 단일 이슈 생성
        IssueCreateRequestDto issueCreateRequestDto = IssueCreateRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .priority(Priority.major)
                .userId(userIdResponseDto.getUserId())
                .build();

        issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());

        // 유저 리스트 생성
        userRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> UserRequestDto.builder()
                        .name("t_name" + i)
                        .email("test_email" + i)
                        .password("t_password" + i)
                        .role(Role.Tester)
                        .build())
                .toList();


        // 이슈 리스트 생성
        issueCreateRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("t_title" + i)
                        .description("t_description" + i)
                        .priority(Priority.major)
                        .userId(userIdResponseDto.getUserId())
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreate : issueCreateRequestDtos) {
            issueIdResponseDto = issueService.createIssue(issueCreate, projectIdResponseDto.getProjectId());
            issueIdResponseDtos.add(issueIdResponseDto);
        }

    }

    @Test
    @DisplayName("유저 프로젝트 할당 성공")
    void 유저프로젝트할당() {
        // given
        AdminRequestDto adminRequestDto = new AdminRequestDto(user.getEmail(), projectIdResponseDto.getProjectId());

        // when
        UserIdResponseDto assignedUserIdResponseDto = adminService.assignUser(adminRequestDto);

        // then
        assertEquals(assignedUserIdResponseDto.getUserId(), userIdResponseDto.getUserId());
    }

    @Test
    @DisplayName("프로젝트 생성 성공")
    void 프로젝트생성() {
        // given
        ProjectRequestDto adminProjectCreate = ProjectRequestDto.builder()
                .title("sample title by admin")
                .description("sample description by admin")
                .build();


        // when
        ProjectIdResponseDto adminProjectDto = adminService.createProject(adminProjectCreate);

        // then
        Project project = projectRepository.findByProjectId(adminProjectDto.getProjectId());
        assertEquals(adminProjectDto.getProjectId(), project.getProjectId());
        assertEquals("sample title by admin", project.getTitle());
        assertEquals("sample description by admin", project.getDescription());
    }

    @Test
    @DisplayName("유저 회원가입 성공")
    void 유저회원가입() {
        // given
        UserRequestDto adminUserCreate = UserRequestDto.builder()
                .name("admin_name")
                .email("test_email_admin")
                .password("t_password_admin")
                .role(Role.Admin)
                .build();

        // when
        UserIdResponseDto adminUserDto = adminService.registerUser(adminUserCreate);

        // then
        User user = userRepository.findByUserIdAndIsDeleted(adminUserDto.getUserId(), false);
        assertEquals(adminUserDto.getUserId(), user.getUserId());
        assertEquals("admin_name", user.getName());
        assertEquals("test_email_admin", user.getEmail());
        assertThat(passwordEncoder.matches("t_password_admin", user.getPassword())).isTrue();
    }


}
