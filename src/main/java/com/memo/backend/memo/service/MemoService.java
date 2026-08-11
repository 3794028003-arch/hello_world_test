package com.memo.backend.memo.service;

import com.memo.backend.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }
}
