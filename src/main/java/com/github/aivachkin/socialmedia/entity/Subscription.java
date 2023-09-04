package com.github.aivachkin.socialmedia.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Класс - подписка
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "subscriptions")
public class Subscription {

    /**
     * id подписки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id подписавшегося пользователя
     */
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    /**
     * id пользователя, получившего запрос на подписку
     */
    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    /**
     * Статус подписки (принято/не принято/отозвано)
     */
    @Column(name = "friend_status")
    private FriendStatus friendStatus;


    /**
     * Подстатус подписки (подписан первый на второго/второй на первого/оба подписаны друг на друга)
     */
    @Column(name = "subs_status")
    private SubStatus subStatus;
}
