package com.example.repository;

import com.example.model.Comment;
import com.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
