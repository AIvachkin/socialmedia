package com.github.aivachkin.socialmedia.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс - сообщение пользователя
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "messages")
public class Message {

    /**
     * id сообщения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id отправителя сообщения
     */
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * id получателя сообщения
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    /**
     * Содержание сообщения
     */
    private String text;

    /**
     * Время отправки сообщения
     */
    private LocalDateTime sendingTime;
}
