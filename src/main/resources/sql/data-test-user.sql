-- Вставка тестовых пользователей
INSERT INTO users (first_name, last_name, password, username)
VALUES ('user1', 'first', '$2y$10$U/zSUQVzFJUoi7a2ckXx1.Nv0cwOup.FXpC.a7985f6p21hseHW8K', 'user1@example.com'),
       ('user2', 'second', '$2y$10$noOu6Qm06SeLRNKL4C63X.z1VqHEr2Gg1I.0SSbw.nn1wlHXNR622', 'user2@example.com'),
       ('user3', 'third', '$2y$10$e.QmYpGgYp.RIqlfKCkaueh219r7VCWL.PzQsqVGAPvsX2MA9oGW2', 'user3@example.com');

INSERT INTO posts (user_id, title, text, created_at)
VALUES (1, 'Title', 'Text', '2023-09-15 20:30:04.111222');

INSERT INTO subscriptions (subscriber_id, target_user_id, friend_status, subs_status)
VALUES (2, 1, 0, 1),
       (1, 3, 1, 2);