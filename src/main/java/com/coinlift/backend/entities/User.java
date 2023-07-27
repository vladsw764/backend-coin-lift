package com.coinlift.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
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
    private Integer followersCount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "to")
    private List<Follower> following = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer followingCount;

    public User(UUID id, String username, String email, String password, Role role, List<Comment> comments, List<Post> posts, List<AuthenticationToken> tokens, List<Follower> followers, Integer followersCount, List<Follower> following, Integer followingCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.comments = comments;
        this.posts = posts;
        this.tokens = tokens;
        this.followers = followers;
        this.followersCount = followersCount;
        this.following = following;
        this.followingCount = followingCount;
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<AuthenticationToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AuthenticationToken> tokens) {
        this.tokens = tokens;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public List<Follower> getFollowing() {
        return following;
    }

    public void setFollowing(List<Follower> following) {
        this.following = following;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }
}
