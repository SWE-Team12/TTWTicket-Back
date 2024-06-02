package com.ttwticket.backend.domain.projects;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectId(Integer projectId);
}
