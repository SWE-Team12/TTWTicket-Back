package com.ttwticket.backend.domain.projects;

import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.projects.ProjectRepository;
import com.ttwticket.backend.domain.projects.dto.ProjectIdResponseDto;
import com.ttwticket.backend.domain.projects.dto.ProjectRequestDto;
import com.ttwticket.backend.domain.projects.dto.ProjectResponseDto;
import com.ttwticket.backend.domain.projects.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

    }

    @Test
    @DisplayName("프로젝트 생성 성공")
    void 프로젝트생성() {
        //given
        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build();

        //when
        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(projectRequestDto);

        //then
        Project project = projectRepository.findByProjectId(projectIdResponseDto.getProjectId());
        assertEquals("sample title", project.getTitle());
        assertEquals("sample description", project.getDescription());
    }

//    @Test
//    @DisplayName("프로젝트 수정 성공")
//    void 프로젝트수정() {
//        //given
//        ProjectRequestDto projectRequestDto = ProjectRequestDto.builder()
//                .title("sample title")
//                .description("sample description")
//                .build();
//
//        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(projectRequestDto);
//        Project project = projectRepository.findByProjectId(projectIdResponseDto.getProjectId());
//
//        ProjectRequestDto modifiedDto = ProjectRequestDto.builder()
//                .title("sample modified title")
//                .description("sample modified description")
//                .build();
//
//        //when
//        projectService.modifyProject(project.getProjectId(), modifiedDto);
//
//        Project modifiedProject = projectRepository.findByProjectId(projectIdResponseDto.getProjectId());
//
//        //then
//        assertEquals("sample modified title", modifiedProject.getTitle());
//        assertEquals("sample modified description", modifiedProject.getDescription());
//    }

    @Test
    @DisplayName("단일 프로젝트 조회 성공")
    void 단일프로젝트조회() {
        //given
        ProjectIdResponseDto projectIdResponseDto = projectService.createProject(ProjectRequestDto.builder()
                .title("sample title")
                .description("sample description")
                .build());

        Integer id = projectIdResponseDto.getProjectId();

        //when
        ProjectResponseDto projectResponseDto = projectService.getProject(id);

        //then
        assertEquals(id, projectResponseDto.getProjectId());
        assertEquals("sample title", projectResponseDto.getTitle());
        assertEquals("sample description", projectResponseDto.getDescription());

    }

    @Test
    @DisplayName("모든 프로젝트 조회 성공")
    void 모든프로젝트조회() {
        //given
        List<Project> projectList = IntStream.rangeClosed(1, 10).mapToObj(i -> Project.builder()
                        .title("test title" + i)
                        .description("test content" + i)
                        .build())
                .collect(Collectors.toList());

        projectRepository.saveAll(projectList);

        //when
        List<ProjectResponseDto> projectResponseDto = projectService.getAllProjects();

        //then
        for (int i=0; i<projectResponseDto.size(); i++) {
            assertEquals("test title" + (i+1), projectResponseDto.get(i).getTitle());
            assertEquals("test content" + (i+1), projectResponseDto.get(i).getDescription());
        }

        assertEquals(projectList.size(), projectResponseDto.size());
    }
}