package com.memo.backend.memo.controller;

import com.memo.backend.common.ApiResponse;
import com.memo.backend.memo.dto.MemoRequest;
import com.memo.backend.memo.dto.MemoResponse;
import com.memo.backend.memo.service.MemoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
public class MemoController {
    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemoResponse> create(@AuthenticationPrincipal String username, @Valid @RequestBody MemoRequest request) {
        return ApiResponse.of(memoService.create(username, request));
    }

    @GetMapping
    public ApiResponse<List<MemoResponse>> findAll(@AuthenticationPrincipal String username,
                                                    @RequestParam(required = false) String keyword,
                                                    @RequestParam(required = false) String category,
                                                    @RequestParam(required = false) Boolean favorite,
                                                    @RequestParam(required = false) Boolean archived) {
        return ApiResponse.of(memoService.findAll(username, keyword, category, favorite, archived));
    }

    @PutMapping("/{id}")
    public ApiResponse<MemoResponse> update(@PathVariable Long id, @AuthenticationPrincipal String username,
                               @Valid @RequestBody MemoRequest request) {
        return ApiResponse.of(memoService.update(id, username, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String username) {
        memoService.delete(id, username);
        return ApiResponse.of(null);
    }

    @PatchMapping("/{id}/favorite")
    public ApiResponse<MemoResponse> toggleFavorite(@PathVariable Long id, @AuthenticationPrincipal String username) {
        return ApiResponse.of(memoService.toggleFavorite(id, username));
    }

    @PatchMapping("/{id}/pin")
    public ApiResponse<MemoResponse> togglePinned(@PathVariable Long id, @AuthenticationPrincipal String username) {
        return ApiResponse.of(memoService.togglePinned(id, username));
    }

    @PatchMapping("/{id}/archive")
    public ApiResponse<MemoResponse> toggleArchived(@PathVariable Long id, @AuthenticationPrincipal String username) {
        return ApiResponse.of(memoService.toggleArchived(id, username));
    }
}
