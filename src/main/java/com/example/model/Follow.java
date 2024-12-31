package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Setter
@Getter
public class Follow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User following;

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + (follower != null ? "User{username='" + follower.getUsername() + "'}" : "null") +
                ", following=" + (following != null ? "User{username='" + following.getUsername() + "'}" : "null") +
                '}';
    }
    // Getters and Setters
}

