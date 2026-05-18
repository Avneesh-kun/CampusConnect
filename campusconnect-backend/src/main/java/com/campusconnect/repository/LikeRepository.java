package com.campusconnect.repository;

import com.campusconnect.model.Like;
import com.campusconnect.model.Post;
import com.campusconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    List<Like> findByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
    long countByPost(Post post);
}