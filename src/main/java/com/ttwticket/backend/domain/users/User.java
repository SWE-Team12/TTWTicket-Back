package com.ttwticket.backend.domain.users;

import com.ttwticket.backend.domain.BaseTimeEntity;
import com.ttwticket.backend.domain.projects.Project;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

//    @ManyToOne
//    @JoinColumn(name = "projectId", nullable = true)
//    private Project project;

    @Column
    private Integer projectId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Boolean isDeleted;

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Builder
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isDeleted = false;
    }

    @Builder
    public User(Integer projectId, String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isDeleted = false;
        this.projectId = projectId;
    }

    public static User createUser(UserRequestDto userRequestDto, String password) {
        return new User(userRequestDto.getName(), userRequestDto.getEmail(), password, userRequestDto.getRole());
    }
}
