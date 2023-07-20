package com.coinlift.backend.repositories;

import com.coinlift.backend.entities.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<AuthenticationToken, Long> {
    @Query("""
        SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id
        WHERE u.id = :userId AND (t.expired = FALSE OR t.revoked = FALSE)
    """)
    List<AuthenticationToken> findAllValidTokenByUserId(UUID userId);

    Optional<AuthenticationToken> findByToken(String token);
}
