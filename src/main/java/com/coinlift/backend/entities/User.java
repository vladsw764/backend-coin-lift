package com.coinlift.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<AuthenticationToken> tokens;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "from")
    private List<Follower> followers = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer followers_count;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "to")
    private List<Follower> following = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer following_count;


    public User(UUID id, String username, String email, String password, Role role, List<Comment> comments, List<Post> posts, List<AuthenticationToken> tokens, List<Follower> followers, Integer followers_count, List<Follower> following, Integer following_count) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.comments = comments;
        this.posts = posts;
        this.tokens = tokens;
        this.followers = followers;
        this.followers_count = followers_count;
        this.following = following;
        this.following_count = following_count;
    }

    public User() {
    }
}
