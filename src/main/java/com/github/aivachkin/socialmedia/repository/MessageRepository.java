package com.github.aivachkin.socialmedia.repository;

import com.github.aivachkin.socialmedia.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


}
