package com.example.service;

import com.example.dto.CommentDto;
import com.example.model.Comment;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class CommentService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Comment addCommentToPost(Long postId, CommentDto commentDto, Long userId) {
        // Fetch the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post with ID " + postId + " not found"));

        // Fetch the authenticated user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        // Create and populate the Comment entity
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        // Save the comment
        return commentRepository.save(comment);
    }
}
