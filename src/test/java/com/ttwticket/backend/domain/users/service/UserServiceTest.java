package com.ttwticket.backend.domain.users.service;

import com.ttwticket.backend.domain.users.Role;
import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

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
    void 회원가입() {
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
    @DisplayName("로그인 성공")
    void 로그인() throws SQLException {
        // given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("sname")
                .email("semail")
                .password("spassword")
                .role(Role.PL)
                .build();

        userService.registerUser(userRequestDto);

        // when
        UserLoginRequestDto userLoginRequestDto = UserLoginRequestDto.builder()
                .email("semail")
                .password("spassword")
                .build();

        UserLoginResponseDto userLoginResponseDto = userService.login(userLoginRequestDto);

        // then
        assertNotNull(userLoginResponseDto.getToken());
    }

    @Test
    @DisplayName("모든 유저 조회 성공")
    void 모든유저조회() {
        // given
        List<User> userList = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> User.builder()
                        .name("test" + i)
                        .email("test email" + i)
                        .password("test password" + i)
                        .role(Role.PL)
                        .build())
                .collect(Collectors.toList());
        userRepository.saveAll(userList);

        // when
        List<UserResponseDto> userResponseDto = userService.getAllUsers();

        // then
        for (int i=0; i<userResponseDto.size(); i++) {
            User user = userRepository.findByUserIdAndIsDeleted(userResponseDto.get(i).getUserId(), false);
            assertEquals("test" + (i+1), userResponseDto.get(i).getName());
            assertEquals("test email" + (i+1), userResponseDto.get(i).getEmail());
            assertEquals("test password" + (i+1), userList.get(i).getPassword());
            assertEquals(Role.PL, user.getRole());
        }

        assertEquals(userList.size(), userResponseDto.size());
    }

    @Test
    @DisplayName("유저 유효성 검사 성공")
    void 유저유효성검사() throws SQLException {
        // given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("sname")
                .email("semail")
                .password("spassword")
                .role(Role.PL)
                .build();

        userService.registerUser(userRequestDto);

        // when
        User user = userService.userValid("semail");

        // then
        assertNotNull(user);
        assertEquals("sname", user.getName());
        assertEquals("semail", user.getEmail());
        assertThat(passwordEncoder.matches("spassword", user.getPassword())).isTrue();
        assertEquals(Role.PL, user.getRole());
        assertFalse(user.getIsDeleted());

    }

    @Test
    @DisplayName("이메일로 유저 찾기 성공")
    void 이메일로유저찾기() throws SQLException {
        //given
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("sname")
                .email("semail")
                .password("spassword")
                .role(Role.PL)
                .build();

        userService.registerUser(userRequestDto);

        //when
        User user = userService.findUserByEmail("semail");

        //then
        assertNotNull(user);
        assertEquals("sname", user.getName());
        assertEquals("semail", user.getEmail());
        assertTrue(passwordEncoder.matches("spassword", user.getPassword()));
        assertEquals(Role.PL, user.getRole());
    }
}