package com.coinlift.communityservice.model;

import com.coinlift.userservice.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String imageLink;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
//    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USERS_POSTS"))
//    private User user;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}