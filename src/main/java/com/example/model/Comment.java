package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + (user != null ? "User{username='" + user.getUsername() + "'}" : "null") +
                ", post=" + (post != null ? "Post{content='" + post.getContent() + "'}" : "null") +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
    // Getters and Setters
}

