package com.memo.backend.memo.dto;

import com.memo.backend.memo.entity.Memo;

import java.time.LocalDateTime;

public record MemoResponse(Long id, String title, String content, String category, boolean isFavorite, boolean isPinned,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(memo.getId(), memo.getTitle(), memo.getContent(), memo.getCategory(), memo.isFavorite(),
                memo.isPinned(), memo.getCreatedAt(), memo.getUpdatedAt());
    }
}
