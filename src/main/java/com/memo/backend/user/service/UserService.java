package com.memo.backend.user.service;

import com.memo.backend.common.exception.PasswordIncorrectException;
import com.memo.backend.common.exception.UserAlreadyExistsException;
import com.memo.backend.common.exception.UserNotFoundException;
import com.memo.backend.config.JwtUtil;
import com.memo.backend.user.dto.LoginRequest;
import com.memo.backend.user.dto.LoginResponse;
import com.memo.backend.user.dto.RegisterRequest;
import com.memo.backend.user.dto.UserResponse;
import com.memo.backend.user.entity.User;
import com.memo.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(request.username());
        }
        User user = userRepository.save(new User(request.username(), request.email(), passwordEncoder.encode(request.password())));
        return UserResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException(request.username()));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordIncorrectException();
        }
        return new LoginResponse(jwtUtil.generateToken(user.getUsername()));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
