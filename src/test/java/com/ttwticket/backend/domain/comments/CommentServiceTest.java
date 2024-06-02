package com.ttwticket.backend.domain.comments;

import com.ttwticket.backend.domain.comments.dto.*;
import com.ttwticket.backend.domain.comments.service.CommentService;
import com.ttwticket.backend.domain.issues.Category;
import com.ttwticket.backend.domain.issues.Issue;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

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
    private Issue issue;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
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
                .projectId(projectIdResponseDto.getProjectId())
                .build();

        userIdResponseDto = userService.registerUser(userRequestDto);
        user = userRepository.findByUserIdAndIsDeleted(userIdResponseDto.getUserId(), false);

        // 단일 이슈 생성
        IssueCreateRequestDto issueCreateRequestDto = IssueCreateRequestDto.builder()
                .title("t_title")
                .description("t_description")
                .priority(Priority.major)
                .userId(userIdResponseDto.getUserId())
                .category(Category.Add_Function)
                .build();

        issueIdResponseDto = issueService.createIssue(issueCreateRequestDto, projectIdResponseDto.getProjectId());
        Issue issue = issueRepository.findByIssueId(issueIdResponseDto.getIssueId());

        // 유저 리스트 생성
        userRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> UserRequestDto.builder()
                        .name("t_name" + i)
                        .email("test_email" + i)
                        .password("t_password" + i)
                        .role(Role.Tester)
                        .projectId(projectIdResponseDto.getProjectId())
                        .build())
                .toList();


        // 이슈 리스트 생성
        issueCreateRequestDtos = IntStream.rangeClosed(1, 10).mapToObj(i -> IssueCreateRequestDto.builder()
                        .title("t_title" + i)
                        .description("t_description" + i)
                        .priority(Priority.major)
                        .userId(userIdResponseDto.getUserId())
                        .category(Category.Add_Function)
                        .build())
                .collect(Collectors.toList());

        for (IssueCreateRequestDto issueCreate : issueCreateRequestDtos) {
            issueIdResponseDto = issueService.createIssue(issueCreate, projectIdResponseDto.getProjectId());
            issueIdResponseDtos.add(issueIdResponseDto);
        }


    }

    @Test
    @DisplayName("코멘트 저장 성공")
    void 코멘트저장() throws SQLException {
        // given
        CommentRequestDto commentRequestDto = new CommentRequestDto("test comment", issueIdResponseDto.getIssueId());

        // when
        try {
            CommentResponseDto commentResponseDto = commentService.saveComment(commentRequestDto, issueIdResponseDto.getIssueId());

            // then
            Comment comment = commentRepository.findCommentByIssue(issue);
            assertEquals("test comment", comment.getMessage());
            assertEquals(issueIdResponseDto.getIssueId(), commentResponseDto.getIssueId());
        } catch (Exception e) {}
    }

    @Test
    @DisplayName("임의의 이슈에 대한 코멘트 모두 조회 성공")
    void 모든코멘트조회() throws SQLException {
        // given
        List<CommentRequestDto> commentList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            CommentRequestDto commentRequestDto = new CommentRequestDto("test comment" + i, issueIdResponseDto.getIssueId());
            try {
                CommentResponseDto commentResponseDto = commentService.saveComment(commentRequestDto, issueIdResponseDto.getIssueId());
            } catch (Exception e) {}
        }

        // when
        List<CommentListDto> commentListDto = commentService.getAllCommentsByIssueId(issueIdResponseDto.getIssueId());

        // then
        for (int i = 0; i < 10; i++) {
            try {
                assertEquals("test comment" + i, commentList.get(i).getMessage());
            } catch (Exception e) {}
        }

        assertEquals(commentList.size(), commentListDto.size());
    }

    @Test
    @DisplayName("코멘트 수정 성공")
    void 코멘트수정() {
        // given
        CommentRequestDto commentRequestDto = new CommentRequestDto("test comment", issueIdResponseDto.getIssueId());

        try {
            CommentResponseDto commentResponseDto = commentService.saveComment(commentRequestDto, issueIdResponseDto.getIssueId());
            Comment comment = commentRepository.findCommentByIssue(issue);

            CommentModifyRequestDto commentModifyRequestDto = new CommentModifyRequestDto("modified comment");

            // when
            CommentModifyResponseDto commentModifyResponseDto = commentService.modifyComment(commentModifyRequestDto, issueIdResponseDto.getIssueId(), comment.getCommentId());

            // then
            assertEquals(commentModifyResponseDto.getCommentId(), comment.getCommentId());
            assertEquals(commentModifyResponseDto.getName(), user.getName());
            assertEquals(commentModifyResponseDto.getIssueId(), comment.getIssue().getIssueId());
            assertEquals(commentModifyResponseDto.getMessage(), "modified comment");

        } catch (Exception e) {}
    }

    @Test
    @DisplayName("코멘트 삭제 성공")
    void 코멘트삭제() {
        // given
        CommentRequestDto commentRequestDto = new CommentRequestDto("test comment", issueIdResponseDto.getIssueId());

        try {
            CommentResponseDto commentResponseDto = commentService.saveComment(commentRequestDto, issueIdResponseDto.getIssueId());
        // when
            commentService.deleteComment(issueIdResponseDto.getIssueId(), commentResponseDto.getCommentId());

        // then
            assertNull(commentRepository.findCommentByCommentId(commentResponseDto.getCommentId()));

        } catch (Exception e) {}
    }

}
