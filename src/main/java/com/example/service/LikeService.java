package com.example.service;


import com.example.model.Like;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.LikeRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LikeService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;



    public Like likePost(Long postId, Long userId) {
        // Fetch the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post with ID " + postId + " not found"));

        // Fetch the authenticated user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        // Check if the user has already liked the post
        boolean alreadyLiked = likeRepository.existsByUserAndPost(user, post);
        if (alreadyLiked) {
            throw new IllegalArgumentException("You have already liked this post");
        }
        // Create and save a new Like
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        return likeRepository.save(like);
    }
}
