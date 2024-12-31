package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class Like implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + (user != null ? "User{username='" + user.getUsername() + "'}" : "null") +
                ", post=" + (post != null ? "Post{content='" + post.getContent() + "'}" : "null") +
                '}';
    }
    // Getters and Setters
}

