package com.memo.backend.memo.repository;

import com.memo.backend.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @EntityGraph(attributePaths = "user")
    List<Memo> findAllByUserUsernameAndTitleContainingIgnoreCaseOrderByIsPinnedDescUpdatedAtDesc(String username, String keyword);

    @EntityGraph(attributePaths = "user")
    List<Memo> findAllByUserUsernameAndCategoryOrderByIsPinnedDescUpdatedAtDesc(String username, String category);

    @EntityGraph(attributePaths = "user")
    List<Memo> findAllByUserUsernameAndIsFavoriteOrderByIsPinnedDescUpdatedAtDesc(String username, boolean favorite);

    @EntityGraph(attributePaths = "user")
    List<Memo> findAllByUserUsernameOrderByIsPinnedDescUpdatedAtDesc(String username);

    Optional<Memo> findByIdAndUserUsername(Long id, String username);
}
