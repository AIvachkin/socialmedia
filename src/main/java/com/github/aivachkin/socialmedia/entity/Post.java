package com.github.aivachkin.socialmedia.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Класс - пост пользователя
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "posts")
public class Post {

    /**
     * id поста
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id пользователя
     */
//    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    /**
     * Заголовок поста
     */
    private String title;

    /**
     * Текс (содержание) поста
     */
    private String text;

    /**
     * Дата и время создания поста
     */
    private LocalDateTime createdAt;

    /**
     * Изображения, прикрепленные к посту
     */
    @OneToOne
    @JoinColumn(name = "id")
    private PostImage image;

}
