package com.memo.backend.memo.service;

import com.memo.backend.common.exception.MemoNotFoundException;
import com.memo.backend.memo.dto.MemoRequest;
import com.memo.backend.memo.dto.MemoResponse;
import com.memo.backend.memo.entity.Memo;
import com.memo.backend.memo.repository.MemoRepository;
import com.memo.backend.user.entity.User;
import com.memo.backend.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserService userService;

    public MemoService(MemoRepository memoRepository, UserService userService) {
        this.memoRepository = memoRepository;
        this.userService = userService;
    }

    public MemoResponse create(String username, MemoRequest request) {
        User user = userService.getByUsername(username);
        return MemoResponse.from(memoRepository.save(new Memo(user, request.title(), request.content())));
    }

    public List<MemoResponse> findAll(String username) {
        return memoRepository.findAllByUserUsernameOrderByUpdatedAtDesc(username).stream().map(MemoResponse::from).toList();
    }

    public MemoResponse update(Long id, String username, MemoRequest request) {
        Memo memo = findOwned(id, username);
        memo.update(request.title(), request.content());
        return MemoResponse.from(memoRepository.save(memo));
    }

    public void delete(Long id, String username) {
        memoRepository.delete(findOwned(id, username));
    }

    private Memo findOwned(Long id, String username) {
        return memoRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new MemoNotFoundException(id));
    }
}
