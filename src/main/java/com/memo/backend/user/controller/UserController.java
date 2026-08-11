package com.memo.backend.user.controller;

import com.memo.backend.common.ApiResponse;
import com.memo.backend.user.dto.UserResponse;
import com.memo.backend.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(@AuthenticationPrincipal String username) {
        return ApiResponse.of(userService.me(username));
    }
}
