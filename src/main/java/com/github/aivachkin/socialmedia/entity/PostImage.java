package com.github.aivachkin.socialmedia.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Класс - изображения, прикрепленные к посту
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "post_images")
public class PostImage {

    /**
     * id изображения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Пост, относящийся к изображению
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * Имя файла
     */
    private String fileName;

}
