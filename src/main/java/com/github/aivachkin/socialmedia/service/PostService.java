package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.dto.post.*;
import com.github.aivachkin.socialmedia.entity.*;
import com.github.aivachkin.socialmedia.exception.PostNotFoundException;
import com.github.aivachkin.socialmedia.mapper.PostMapper;
import com.github.aivachkin.socialmedia.repository.PostImageRepository;
import com.github.aivachkin.socialmedia.repository.PostRepository;
import com.github.aivachkin.socialmedia.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с постами пользователей
 */
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private final PostImageRepository postImageRepository;
    private final SubscriptionRepository subscriptionRepository;

    private final UserService userService;

    @Value("${path.to.file.folder}")
    private String filePath;


    /**
     * Метод для создания поста
     *
     * @param createPostRequest ДТО - запрос на создание поста
     * @return ДТО - ответ после создания поста
     */
    public CreatePostResponse createPost(CreatePostRequest createPostRequest) {

        Post post = new Post()
                .setText(createPostRequest.getText())
                .setTitle(createPostRequest.getTitle())
                .setUserId(userService.getAuthenticatedUserId())
                .setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        return postMapper.toCreatePostResponse(post);
    }

    /**
     * Метод для получения постов пользователя с постраничным выводом
     *
     * @param userId id пользователя
     * @param offset номер страницы
     * @param limit  количество постов на странице
     * @return список ДТО - посты пользователя
     */
    public Page<PostDto> getPostsByUserId(Long userId, int offset, int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
        Page<Post> postsPage = postRepository.findByUserId(userId, pageable);

        return postsPage.map(post -> postMapper.toPostDto(post, userId));

    }

    /**
     * Метод для установки или обновления картинки поста
     *
     * @param postId id поста
     * @param image  файл (картинка)
     * @return ответ об успешности выполнения (true или false)
     */
    public Boolean updatePostImage(Long postId, MultipartFile image) throws IOException {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Пост с id " + postId + " не найден"));

        Path path = Path.of(filePath, postId + "_image." + getExtensions(image.getOriginalFilename()));
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);

        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(path, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        PostImage postImage = post.getImage();
        if (postImage == null) {
            postImage = new PostImage();
            postImage.setPost(post);
        }

        postImage.setImage(path.toString());
        postImage.setFileSize(image.getSize());
        postImage.setMediaType(image.getContentType());

        postImageRepository.save(postImage);
        post.setImage(postImage);
        postRepository.save(post);


        return true;

    }


    /**
     * Метод для обновления поста
     *
     * @param postId        id поста
     * @param updatePostDTO ДТО - запрос на редактирование поста
     * @return ДТО - ответ после редактирования поста
     */
    public CreatePostResponse updatePost(Long postId, UpdatePostDto updatePostDTO) {

        Post post = postRepository.findByUserIdAndId(userService.getAuthenticatedUserId(), postId).orElseThrow(
                () -> new PostNotFoundException("Пост не найден в БД или он принадлежит другому пользователю"));

        if (updatePostDTO.getTitle() != null) {
            post.setTitle(updatePostDTO.getTitle());
        }
        if (updatePostDTO.getText() != null) {
            post.setText(updatePostDTO.getText());
        }

        postRepository.save(post);

        return postMapper.toCreatePostResponse(post);
    }

    /**
     * Метод для удаления поста пользователя
     *
     * @param postId id поста
     */
    public void deletePost(Long postId) {

        Post post = postRepository.findByUserIdAndId(userService.getAuthenticatedUserId(), postId).orElseThrow(
                () -> new PostNotFoundException("Пост не найден в БД или он принадлежит другому пользователю"));

        postRepository.delete(post);
    }

    /**
     * Метод для постраничного вывода постов пользователей, на которых подписан текущий пользователь
     *
     * @param offset номер страницы
     * @param limit количество постов на странице
     * @return список постов
     */
    public Page<PostDto> getUserActivityFeed(int offset, int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdAt").descending());

        List<Subscription> subscriptions =
                subscriptionRepository.findBySubscriberIdAndSubscriptionStatusIn(userService.getAuthenticatedUserId(),
                        SubStatus.USER1, SubStatus.BOTH);

        List<Long> targetUserIds = subscriptions.stream()
                .map(Subscription::getTargetUser)
                .map(User::getId)
                .toList();

        Page<Post> activityFeedPosts = postRepository.findByUserIdIn(targetUserIds, pageable);

        return activityFeedPosts.map(postMapper::toPostTargetUserDto);
    }

    /**
     * Метод возвращает расширение файла
     *
     * @param fileName имя файла
     * @return расширение файла
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}


