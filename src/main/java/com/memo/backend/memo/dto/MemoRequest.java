package com.memo.backend.memo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemoRequest(@NotBlank @Size(max = 200) String title, @NotBlank String content,
                          @Size(max = 100) String category) {
}
