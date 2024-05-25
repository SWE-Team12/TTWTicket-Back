package com.ttwticket.backend.domain.users.service;

import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerUser() {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("sname")
                .email("semail")
                .password("spassword")
                .role(Role.PL)
                .build();


        try {
            //when
            UserIdResponseDto userIdResponseDto = userService.registerUser(userRequestDto);

            //then
            User user = userRepository.findByUserIdAndIsDeleted(userIdResponseDto.getUserId(), false);
            assertEquals("sname", user.getName());
            assertEquals("semail", user.getEmail());
            //("sample password", passwordEncoder.encode(user.getPassword()));
            assertThat(passwordEncoder.matches("spassword", user.getPassword())).isTrue();
            assertEquals(Role.PL, user.getRole());

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    void login() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void userValid() {
    }

    @Test
    void findUserByEmail() {
    }
}