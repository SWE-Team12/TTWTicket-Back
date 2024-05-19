package com.ttwticket.backend.domain.projects;

import com.ttwticket.backend.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 엔티티의 식별자 값을 자동으로 생성
    private Long projectId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
}
