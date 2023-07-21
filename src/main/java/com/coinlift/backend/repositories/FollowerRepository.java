package com.coinlift.backend.repositories;

import com.coinlift.backend.dtos.users.FollowerResponseDto;
import com.coinlift.backend.entities.Follower;
import com.coinlift.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FollowerRepository extends JpaRepository<Follower, UUID> {

    Follower findByFromAndTo(User from, User to);

    Boolean existsByFrom_IdAndTo_Id(UUID fromId, UUID toId);

    @Query("""
            SELECT NEW com.coinlift.backend.dtos.users.FollowerResponseDto(f.to.username, f.to.id)
            FROM Follower f WHERE f.from.id = :fromId
            """)
    List<FollowerResponseDto> findAllByFromId(UUID fromId);

    @Query("""
            SELECT NEW com.coinlift.backend.dtos.users.FollowerResponseDto(f.from.username, f.from.id)
            FROM Follower f WHERE f.to.id = :toId
            """)
    List<FollowerResponseDto> findAllByToId(UUID toId);
}
