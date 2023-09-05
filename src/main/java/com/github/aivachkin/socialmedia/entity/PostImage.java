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
    @Column(name="image_id")
    private Long id;

    /**
     * Пост, относящийся к изображению
     */
    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * Ссылка на файл
     */
    @Column(name="image_link")
    private String image;

    /**
     * Размер изображения
     */
    private Long fileSize;

    /**
     * Тип файла изображения
     */
    private String mediaType;

}
