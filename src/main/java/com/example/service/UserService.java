package com.example.service;

import com.example.configuration.Config;
import com.example.dto.RegisterDto;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Config config;
    public User addUser(RegisterDto userDto) throws Exception {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new Exception("Username already exists");
        }
        // Check if the email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new Exception("Email already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    public User findUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: "+ id));
    }
    @Cacheable(value = "userSearchCache", key = "#keyword + '-' + #page + '-' + #size")
    public Map<String, Object> searchUsers(String keyword, int page, int size) {
        // Fetch users from the repository
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> users = userRepository.searchUsers(keyword, pageable);

        // Format user data
        List<Map<String, Object>> formattedUsers = users.getContent().stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getId());
                    map.put("username", user.getUsername());
                    map.put("email", user.getEmail());
                    map.put("bio", user.getBio());
                    return map;
                })
                .collect(Collectors.toList());

        // Prepare and return the response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("totalElements", users.getTotalElements());
        responseData.put("totalPages", users.getTotalPages());
        responseData.put("currentPage", users.getNumber());
        responseData.put("users", formattedUsers);

        return responseData;
    }

    public String uploadProfilePicture(Long userId, MultipartFile file) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: "+ userId));

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        File uploadDir = new File(config.getFilePath());
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadDir, fileName);

        file.transferTo(destinationFile);

        user.setPicture(config.getFilePath() + fileName); // Assuming 'bio' is temporarily used for file path
        userRepository.save(user);

        return config.getFilePath() + fileName;
    }
}

