package com.example.repository;

import com.example.model.Follow;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);

    @Query("SELECT u FROM Follow f JOIN f.follower u WHERE f.following.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM Follow f JOIN f.following u WHERE f.follower.id = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);
}
