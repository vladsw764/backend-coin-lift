package com.coinlift.backend.entities;

import jakarta.persistence.*;

import java.util.UUID;

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

    public Follower() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}
