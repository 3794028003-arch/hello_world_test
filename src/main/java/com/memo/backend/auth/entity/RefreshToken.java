package com.memo.backend.auth.entity;

import com.memo.backend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens", indexes = @Index(name = "idx_refresh_tokens_token_id", columnList = "token_id", unique = true))
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "token_id", nullable = false, unique = true, updatable = false)
    private String tokenId;
    @Column(nullable = false, updatable = false)
    private Instant expiresAt;

    protected RefreshToken() { }
    public RefreshToken(User user, String tokenId, Instant expiresAt) { this.user = user; this.tokenId = tokenId; this.expiresAt = expiresAt; }
    public String getTokenId() { return tokenId; }
    public Instant getExpiresAt() { return expiresAt; }
}
