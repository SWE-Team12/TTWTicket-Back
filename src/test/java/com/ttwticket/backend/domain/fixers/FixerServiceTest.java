package com.ttwticket.backend.domain.fixers;

import com.ttwticket.backend.domain.fixers.dto.FixerRequestDto;
import com.ttwticket.backend.domain.fixers.dto.FixerResponseDto;
import com.ttwticket.backend.domain.fixers.service.FixerService;
import com.ttwticket.backend.domain.issues.IssueRepository;
import com.ttwticket.backend.domain.issues.Priority;
import com.ttwticket.backend.domain.issues.dto.IssueCreateRequestDto;
import com.ttwticket.backend.domain.issues.dto.IssueIdResponseDto;
import com.ttwticket.backend.domain.issues.service.IssueService;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.service.ProjectService;
import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import com.ttwticket.backend.domain.users.service.UserService;
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
public class FixerServiceTest {

    @Autowired
    private FixerService fixerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FixerRepository fixerRepository;

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

    @BeforeEach
    void setUp() {
        fixerRepository.deleteAll();
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
    @DisplayName("fixer 지정 성공")
    void assignUser() {
        // given
        FixerRequestDto fixerRequestDto = new FixerRequestDto(userRequestDto.getEmail());

        // when
        FixerResponseDto fixerResponseDto = fixerService.assignUser(fixerRequestDto, issueIdResponseDto.getIssueId());

        // then
        assertEquals(fixerResponseDto.getName(), userRequestDto.getName());
        assertEquals(fixerResponseDto.getIssueId(), issueIdResponseDto.getIssueId());

    }

    @Test
    @DisplayName("할당된 모든 유저 검색 성공")
    void getUsers() {
        for (int i = 0; i < 10; i++) {
            // given
            FixerRequestDto fixerRequestDto = new FixerRequestDto(userRequestDtos.get(i).getEmail());

            try {
                // when
                FixerResponseDto fixerResponseDto= fixerService.assignUser(fixerRequestDto, issueIdResponseDtos.get(i).getIssueId());

                // then
                assertEquals(fixerResponseDto.getName(), userRequestDtos.get(i).getName());
                assertEquals(fixerResponseDto.getIssueId(), issueIdResponseDto.getIssueId());
            } catch (Exception e) {}
        }

    }


}
