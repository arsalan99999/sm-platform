package com.example.controller;

import com.example.auth.AuthService;
import com.example.dto.*;
import com.example.model.Comment;
import com.example.model.Like;
import com.example.model.Post;
import com.example.model.User;
import com.example.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1")
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @PostMapping("/users/register")
    public Response signUp(@RequestBody RegisterDto registerDto) {
        try {
            User savedUser = userService.addUser(registerDto);
            return new Response(true, "User registered successfully", "200", savedUser);
        } catch (Exception e) {
            return new Response(false, "Error while registering user", "500", e.getMessage());
        }
    }

    @PostMapping("/users/login")
    public Response login(@RequestBody LoginDto loginDto) {
        try {
            String jwtToken = authService.login(loginDto.getUsername(), loginDto.getPassword());
            return new Response(true, "Login Successful", "200", jwtToken);
        } catch (Exception e) {
            return new Response(false, "Authentication failed: " + e.getMessage(), "500", null);
        }
    }

    @GetMapping("/users/{id}")
    public Response getUserById(@PathVariable Long id) {
        try {
            User user = userService.findUserById(id);
            return new Response(true, "User retrieved successfully", "200",user);

        }  catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (Exception e) {
            return new Response(false, "Failed to retrieve user", "500",e.getMessage());
        }
    }

    @PostMapping("/posts")
    public Response createPost(@RequestBody PostDto postDto, HttpServletRequest request) {
        try {
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
            Post createdPost = postService.createPost(postDto, authenticatedUserId);
            return new Response(true, "Post created successfully", "200", createdPost);
        } catch (IllegalArgumentException e) {
            return new Response(false, e.getMessage(), "400", null);
        } catch (AccessDeniedException e){
            return new Response(false, e.getMessage(), "403",null);
        } catch (Exception e) {
            return new Response(false, "Failed to create post", "500", e.getMessage());
        }
    }

    @GetMapping("/posts")
    public Response getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy) {
        try {

            Map<String, Object> responseData = postService.getFormattedPosts(page, size, sortBy);

            return new Response(true, "Posts retrieved successfully", "200", responseData);
        } catch (Exception e) {

            return new Response(false, "Failed to retrieve posts", "500", e.getMessage());
        }
    }

    @GetMapping("/posts/{id}")
    public Response getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);

            return new Response(true, "Post retrieved successfully", "200", post);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (Exception e) {
            return new Response(false, "Failed to retrieve post", "500", e.getMessage());
        }
    }

    @PutMapping("/posts/{id}")
    public Response updatePost(@PathVariable Long id, @RequestBody PostDto updatedPostDto, HttpServletRequest request){
        try {
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
            Post existingPost = postService.getPostById(id);

            if (!existingPost.getUser().getId().equals(authenticatedUserId)) {
                throw new AccessDeniedException("You are not authorized to update this post");
            }
            Post updatedPost = postService.updatePost(existingPost, updatedPostDto);

            return new Response(true, "Post updated successfully", "200", updatedPost);

        } catch (AccessDeniedException e) {
            return new Response(false, e.getMessage(), "403",null);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (Exception e) {
            return new Response(false, "Failed to update post", "500", e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}")
    public Response deletePost(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
            Post existingPost = postService.getPostById(id);

            if (!existingPost.getUser().getId().equals(authenticatedUserId)) {
                throw new AccessDeniedException("You are not authorized to delete this post");
            }
            postService.deletePostById(id);

            return new Response(true, "Post deleted successfully", "200", null);

        } catch (AccessDeniedException e) {
            return new Response(false, e.getMessage(), "403", null);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (Exception e) {
            return new Response(false, "Failed to delete post", "500", e.getMessage());
        }
    }

    @PostMapping("/posts/{id}/comments")
    public Response addCommentToPost(@PathVariable Long id, @RequestBody CommentDto commentDto, HttpServletRequest request) {
        try {
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");

            Comment createdComment = commentService.addCommentToPost(id, commentDto, authenticatedUserId);

            return new Response(true, "Comment added successfully", "200", createdComment);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (IllegalArgumentException e) {
            return new Response(false, e.getMessage(), "400", null);
        } catch (Exception e) {
            return new Response(false, "Failed to add comment", "500", e.getMessage());
        }
    }
    @PostMapping("/posts/{id}/like")
    public Response likePost(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
            Like like = likeService.likePost(id, authenticatedUserId);

            return new Response(true, "Post liked successfully", "200", like);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (IllegalArgumentException e) {
            return new Response(false, e.getMessage(), "400", null);
        } catch (Exception e) {
            return new Response(false, "Failed to like post", "500", e.getMessage());
        }
    }

    @PostMapping("/users/{id}/follow")
    public Response followUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
            followService.followUser(authenticatedUserId, id);

            return new Response(true, "User followed successfully", "200", null);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (IllegalArgumentException e) {
            return new Response(false, e.getMessage(), "400", null);
        } catch (Exception e) {
            return new Response(false, "Failed to follow user", "500", e.getMessage());
        }
    }

    @GetMapping("/users/{id}/followers")
    public Response getUserFollowers(@PathVariable Long id) {
        try {
            List<Map<String, Object>> formattedFollowers = followService.getFormattedUserFollowers(id);

            return new Response(true, "Followers retrieved successfully", "200", formattedFollowers);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (Exception e) {
            return new Response(false, "Failed to retrieve followers", "500", e.getMessage());
        }
    }


    @GetMapping("/users/{id}/following")
    public Response getUsersFollowing(@PathVariable Long id) {
        try {
            List<Map<String, Object>> formattedFollowing = followService.getFormattedUsersFollowing(id);

            return new Response(true, "Following users retrieved successfully", "200", formattedFollowing);
        } catch (NoSuchElementException e) {
            return new Response(false, e.getMessage(), "404", null);
        } catch (Exception e) {
            return new Response(false, "Failed to retrieve following users", "500", e.getMessage());
        }
    }

    @PostMapping("/posts/search")
    public Response searchPosts(@RequestBody SearchRequestDto searchRequest,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> responseData = postService.searchPosts(searchRequest.getKeyword(), page, size);

            return new Response(true, "Posts retrieved successfully", "200", responseData);
        } catch (Exception e) {
            return new Response(false, "Failed to retrieve posts", "500", e.getMessage());
        }
    }

    @PostMapping("/users/search")
    public Response searchUsers(@RequestBody SearchRequestDto searchRequest,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> responseData = userService.searchUsers(searchRequest.getKeyword(), page, size);

            return new Response(true, "Users retrieved successfully", "200", responseData);
        } catch (Exception e) {
            return new Response(false, "Failed to retrieve users", "500", e.getMessage());
        }
    }

}
