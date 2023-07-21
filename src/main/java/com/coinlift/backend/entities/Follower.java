package com.coinlift.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "followers")
@Entity
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "from_user_fk", referencedColumnName = "id")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user_fk", referencedColumnName = "id")
    private User to;

    public Follower(User from, User to) {
        this.from = from;
        this.to = to;
    }
}
