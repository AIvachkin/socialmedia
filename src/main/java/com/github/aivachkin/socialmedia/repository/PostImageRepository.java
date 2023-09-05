package com.github.aivachkin.socialmedia.repository;

import com.github.aivachkin.socialmedia.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository <PostImage, Long> {
}
