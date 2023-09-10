package com.github.aivachkin.socialmedia.repository;

import com.github.aivachkin.socialmedia.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByUserIdAndId(Long userId, Long postId);

    Page<Post> findByUserId (Long id, Pageable pageable);

    Optional<Post> findById(Long id);
}
