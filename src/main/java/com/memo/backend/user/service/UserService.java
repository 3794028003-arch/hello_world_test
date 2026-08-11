package com.memo.backend.user.service;

import com.memo.backend.common.exception.PasswordIncorrectException;
import com.memo.backend.common.exception.UserAlreadyExistsException;
import com.memo.backend.common.exception.UserNotFoundException;
import com.memo.backend.common.exception.InvalidTokenException;
import com.memo.backend.auth.entity.RefreshToken;
import com.memo.backend.auth.repository.RefreshTokenRepository;
import com.memo.backend.config.JwtUtil;
import com.memo.backend.user.dto.LoginRequest;
import com.memo.backend.user.dto.LoginResponse;
import com.memo.backend.user.dto.RegisterRequest;
import com.memo.backend.user.dto.UserResponse;
import com.memo.backend.user.entity.User;
import com.memo.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.username());
        }
        User user = userRepository.save(new User(request.username(), request.email(), passwordEncoder.encode(request.password())));
        return UserResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(PasswordIncorrectException::new);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordIncorrectException();
        }
        return issueTokens(user);
    }

    public LoginResponse refresh(String refreshToken) {
        try {
            if (!"refresh".equals(jwtUtil.getTokenType(refreshToken))) throw new InvalidTokenException();
            String tokenId = jwtUtil.getTokenId(refreshToken);
            RefreshToken stored = refreshTokenRepository.findByTokenId(tokenId).orElseThrow(InvalidTokenException::new);
            if (stored.getExpiresAt().isBefore(Instant.now())) {
                refreshTokenRepository.delete(stored);
                throw new InvalidTokenException();
            }
            String username = jwtUtil.getUsername(refreshToken);
            refreshTokenRepository.delete(stored);
            return issueTokens(getByUsername(username));
        } catch (InvalidTokenException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new InvalidTokenException();
        }
    }

    public void logout(String refreshToken) {
        try {
            if ("refresh".equals(jwtUtil.getTokenType(refreshToken))) {
                refreshTokenRepository.deleteByTokenId(jwtUtil.getTokenId(refreshToken));
            }
        } catch (RuntimeException ignored) {
            // Logout is idempotent; an expired or already revoked token remains invalid.
        }
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserResponse me(String username) { return UserResponse.from(getByUsername(username)); }

    private LoginResponse issueTokens(User user) {
        String tokenId = UUID.randomUUID().toString();
        refreshTokenRepository.save(new RefreshToken(user, tokenId, Instant.now().plusMillis(jwtUtil.getRefreshExpirationMillis())));
        return new LoginResponse(jwtUtil.generateAccessToken(user.getUsername()), jwtUtil.generateRefreshToken(user.getUsername(), tokenId));
    }
}
