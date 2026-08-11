package com.memo.backend.memo.controller;

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
    public MemoResponse create(@AuthenticationPrincipal String username, @Valid @RequestBody MemoRequest request) {
        return memoService.create(username, request);
    }

    @GetMapping
    public List<MemoResponse> findAll(@AuthenticationPrincipal String username) {
        return memoService.findAll(username);
    }

    @PutMapping("/{id}")
    public MemoResponse update(@PathVariable Long id, @AuthenticationPrincipal String username,
                               @Valid @RequestBody MemoRequest request) {
        return memoService.update(id, username, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal String username) {
        memoService.delete(id, username);
    }
}
