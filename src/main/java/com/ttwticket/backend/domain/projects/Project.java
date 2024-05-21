package com.ttwticket.backend.domain.projects;

import com.ttwticket.backend.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "project")
@NoArgsConstructor
public class Project extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String description;

    @Builder
    public Project(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
