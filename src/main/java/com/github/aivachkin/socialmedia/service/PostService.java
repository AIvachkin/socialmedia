package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.domain.JwtUser;
import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.dto.post.PostDTO;
import com.github.aivachkin.socialmedia.entity.Post;
import com.github.aivachkin.socialmedia.entity.PostImage;
import com.github.aivachkin.socialmedia.exception.PostNotFoundException;
import com.github.aivachkin.socialmedia.mapper.PostMapper;
import com.github.aivachkin.socialmedia.repository.PostImageRepository;
import com.github.aivachkin.socialmedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

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

    @Value("${path.to.file.folder}")
    private String filePath;


    public CreatePostResponse createPost(CreatePostRequest createPostRequest)  {

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Post post = new Post()
                .setText(createPostRequest.getText())
                .setTitle(createPostRequest.getTitle())
                .setUserId(jwtUser.getUserId())
                .setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

        return postMapper.toCreatePostResponse(post);
    }

    public Page<PostDTO> getPostsByUserId(Long userId, int offset, int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
        Page<Post> postsPage = postRepository.findByUserId(userId, pageable);

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return postsPage.map(post -> postMapper.toPostDto(post, jwtUser.getUsername()));

    }

    public Boolean updatePostImage(Long postId, MultipartFile image) throws IOException {

        Post post = postRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException("Пост с id " + postId + " не найден"));

        Path path = Path.of(filePath, postId + "_image." + getExtensions(image.getOriginalFilename()));
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);

        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(path, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        PostImage postImage = post.getImage();
        if(postImage == null) {
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
     * Метод возвращает расширение файла
     * @param fileName
     * @return
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
