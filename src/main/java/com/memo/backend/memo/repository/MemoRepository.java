package com.memo.backend.memo.repository;

import com.memo.backend.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByUserUsernameOrderByUpdatedAtDesc(String username);

    Optional<Memo> findByIdAndUserUsername(Long id, String username);
}
