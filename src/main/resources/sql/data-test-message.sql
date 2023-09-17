INSERT INTO users (username, password, first_name, last_name)
VALUES ('user1@example.com', '$2y$10$8O.Qd0DeYFrnvEuumS6gjunzs3m0b7Lv6EauQNM124uNKhGTnHNmu', 'user1', 'first'),
       ('user2@example.com', '$2y$10$noOu6Qm06SeLRNKL4C63X.z1VqHEr2Gg1I.0SSbw.nn1wlHXNR622', 'user2', 'second'),
       ('user3@example.com', '$2y$10$e.QmYpGgYp.RIqlfKCkaueh219r7VCWL.PzQsqVGAPvsX2MA9oGW2', 'user3', 'third');

INSERT INTO messages (sender_id, receiver_id, text, sending_time)
VALUES (1, 2, 'message 1', '2023-09-17 10:30:04.111222'),
       (2, 1, 'message 2', '2023-09-17 11:35:02.111222');

INSERT INTO subscriptions (subscriber_id, target_user_id, friend_status, subs_status)
VALUES (2, 1, 0, 1),
       (1, 3, 1, 2);