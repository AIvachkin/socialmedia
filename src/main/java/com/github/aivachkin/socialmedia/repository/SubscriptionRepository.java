package com.github.aivachkin.socialmedia.repository;

import com.github.aivachkin.socialmedia.entity.FriendStatus;
import com.github.aivachkin.socialmedia.entity.SubStatus;
import com.github.aivachkin.socialmedia.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findBySubscriberAndTargetUserAndFriendStatus(
            Long subscriberId, Long targetUserId, FriendStatus friendStatus);


    /**
     * Запрос на получение списка активных подписок пользователя
     *
     * @param subscriberId id пользователя
     * @param status1      подстатус (пользователь - подписчик, подписка на пользователя, обоюдная подписка)
     * @param status2      подстатус (пользователь - подписчик, подписка на пользователя, обоюдная подписка)
     * @return список активных подписок пользователя
     */
    @Query("SELECT s FROM Subscription s " +
            "WHERE (s.subscriber.id = :subscriberId " +
            "AND (s.subStatus = :status1 OR s.subStatus = :status2))")
    List<Subscription> findBySubscriberIdAndSubscriptionStatusIn(@Param("subscriberId") Long subscriberId,
                                                                 @Param("status1") SubStatus status1,
                                                                 @Param("status2") SubStatus status2);

}
