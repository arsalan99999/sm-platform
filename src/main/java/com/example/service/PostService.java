package com.example.service;

import com.example.dto.PostDto;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Post createPost(PostDto postDto, Long authenticatedUserId) throws AccessDeniedException {
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + postDto.getUserId()));

        if (!authenticatedUserId.equals(postDto.getUserId())) {
            throw new AccessDeniedException("You are not authorized to create a post for another user");
        }
        Post post = new Post();
        post.setUser(user);
        post.setContent(postDto.getContent());
        post.setTimestamp(LocalDateTime.now());

        return postRepository.save(post);
    }

    @Cacheable(value = "posts", key = "#page + '-' + #size + '-' + #sortBy")
    public Map<String, Object> getFormattedPosts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<Post> posts = postRepository.findAll(pageable);

        List<Map<String, Object>> formattedPosts = posts.getContent().stream()
                .map(post -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", post.getId());
                    map.put("username", post.getUser().getUsername());
                    map.put("content", post.getContent());
                    map.put("timestamp", post.getTimestamp());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("totalElements", posts.getTotalElements());
        responseData.put("totalPages", posts.getTotalPages());
        responseData.put("currentPage", posts.getNumber());
        responseData.put("posts", formattedPosts);

        return responseData;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found with ID: " + id));
    }

    public Post updatePost(Post existingPost, PostDto updatedPostDto) {
        existingPost.setContent(updatedPostDto.getContent());
        existingPost.setTimestamp(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    public void deletePostById(Long id) {
        Post existingPost = getPostById(id);
        postRepository.delete(existingPost);
    }

    @Cacheable(value = "postSearchCache", key = "#keyword + '-' + #page + '-' + #size")
    public Map<String, Object> searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Post> posts = postRepository.searchByKeyword(keyword, pageable);

        // Format post data
        List<Map<String, Object>> formattedPosts = posts.getContent().stream()
                .map(post -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", post.getId());
                    map.put("username", post.getUser().getUsername());
                    map.put("content", post.getContent());
                    map.put("timestamp", post.getTimestamp());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("totalElements", posts.getTotalElements());
        responseData.put("totalPages", posts.getTotalPages());
        responseData.put("currentPage", posts.getNumber());
        responseData.put("posts", formattedPosts);

        return responseData;
    }



}
