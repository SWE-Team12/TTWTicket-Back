package com.ttwticket.backend.domain.users.service;

import com.ttwticket.backend.domain.users.User;
import com.ttwticket.backend.domain.users.UserRepository;
import com.ttwticket.backend.domain.users.dto.UserIdResponseDto;
import com.ttwticket.backend.domain.users.dto.UserLoginDto;
import com.ttwticket.backend.domain.users.dto.UserRequestDto;
import com.ttwticket.backend.domain.users.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserIdResponseDto registerUser(UserRequestDto userRequestDto) {
        UserIdResponseDto userIdResponseDto = UserIdResponseDto.builder()
                .userId(userRepository.save(userRequestDto.toEntity()).getUserId())
                .build();
        return userIdResponseDto;
    }

    @Transactional
    public User login(UserLoginDto userLoginDto) {
        Optional<User> optionalUser = userRepository.findByEmailAndIsDeleted(userLoginDto.getEmail(), false);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(userLoginDto.getPassword())) {
            return null;
        }

        return user;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

}
