package com.ttwticket.backend.domain.users.controller;

import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.dto.*;
import com.ttwticket.backend.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.processing.SQL;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

//    @PostMapping("/register")
//    public UserIdResponseDto register(@RequestBody UserRequestDto userRequestDto) throws SQLException {
//        return userService.registerUser(userRequestDto);
//    }
    
    @PostMapping("/login")
    public UserLoginResponseDto login(@Validated @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletRequest request) throws SQLException {
        return userService.login(userLoginRequestDto);
    }

    @GetMapping("")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
