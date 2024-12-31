package com.example.service;

import com.example.dto.UserDto;
import com.example.model.Follow;
import com.example.model.User;
import com.example.repository.FollowRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + followerId + " not found"));

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + followingId + " not found"));

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        boolean alreadyFollowing = followRepository.existsByFollowerAndFollowing(follower, following);
        if (alreadyFollowing) {
            throw new IllegalArgumentException("You are already following this user");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }

    public List<Map<String, Object>> getFormattedUserFollowers(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("User with ID " + userId + " not found")
        );

        List<User> followers = followRepository.findFollowersByUserId(userId);

        return followers.stream()
                .map(follower -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", follower.getId());
                    map.put("username", follower.getUsername());
                    return map;
                })
                .collect(Collectors.toList());
    }
    public List<Map<String, Object>> getFormattedUsersFollowing(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("User with ID " + userId + " not found")
        );

        List<User> following = followRepository.findFollowingByUserId(userId);

        return following.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getId());
                    map.put("username", user.getUsername());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
