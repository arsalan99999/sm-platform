package com.example.repository;

import com.example.model.Like;
import com.example.model.Post;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndPost(User user, Post post);
}
