package com.coinlift.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name = "Token")
@Table(name = "auth_token")
public class AuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public AuthenticationToken() {
    }

    public AuthenticationToken(Long id, String token, TokenType tokenType, boolean expired, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
    }
}
