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
        Memo memo = new Memo(user, request.title(), request.content());
        memo.update(request.title(), request.content(), request.category());
        return MemoResponse.from(memoRepository.save(memo));
    }

    public List<MemoResponse> findAll(String username, String keyword, String category, Boolean favorite) {
        List<Memo> memos;
        if (keyword != null && !keyword.isBlank()) {
            memos = memoRepository.findAllByUserUsernameAndTitleContainingIgnoreCaseOrderByIsPinnedDescUpdatedAtDesc(username, keyword);
        } else if (category != null && !category.isBlank()) {
            memos = memoRepository.findAllByUserUsernameAndCategoryOrderByIsPinnedDescUpdatedAtDesc(username, category);
        } else if (favorite != null) {
            memos = memoRepository.findAllByUserUsernameAndIsFavoriteOrderByIsPinnedDescUpdatedAtDesc(username, favorite);
        } else {
            memos = memoRepository.findAllByUserUsernameOrderByIsPinnedDescUpdatedAtDesc(username);
        }
        return memos.stream().map(MemoResponse::from).toList();
    }

    public MemoResponse update(Long id, String username, MemoRequest request) {
        Memo memo = findOwned(id, username);
        memo.update(request.title(), request.content(), request.category());
        return MemoResponse.from(memoRepository.save(memo));
    }

    public void delete(Long id, String username) {
        memoRepository.delete(findOwned(id, username));
    }

    public MemoResponse toggleFavorite(Long id, String username) {
        Memo memo = findOwned(id, username);
        memo.toggleFavorite();
        return MemoResponse.from(memoRepository.save(memo));
    }

    public MemoResponse togglePinned(Long id, String username) {
        Memo memo = findOwned(id, username);
        memo.togglePinned();
        return MemoResponse.from(memoRepository.save(memo));
    }

    private Memo findOwned(Long id, String username) {
        return memoRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new MemoNotFoundException(id));
    }
}
