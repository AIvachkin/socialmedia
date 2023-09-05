package com.github.aivachkin.socialmedia.repository;

import com.github.aivachkin.socialmedia.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByUserIdAndId(Long userId, Long postId);
}
