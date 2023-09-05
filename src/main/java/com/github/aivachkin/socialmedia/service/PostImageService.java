package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.entity.Post;
import com.github.aivachkin.socialmedia.entity.PostImage;
import com.github.aivachkin.socialmedia.repository.PostImageRepository;
import com.github.aivachkin.socialmedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с изображениями для постов
 */
@Service
@RequiredArgsConstructor
public class PostImageService {

//    @Value("${images.location}")
//    private String uploadDirectory;

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;

//    private String createImage(MultipartFile multipartFile) throws IOException {
//
//        String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
//
//        Path filePath = Paths.get(uploadDirectory + fileName);
//        Files.createDirectories(filePath.getParent());
//
//        try {
//            Files.copy(multipartFile.getInputStream(), filePath);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return fileName;
//    }

//    public void saveFiles(List<MultipartFile> files, Post post) {
//        List<PostImage> postImageList = files.stream()
//                .map(multipartFile -> {
//                    try {
//                        return createImage(multipartFile);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .map(fileName -> new PostImage().setFileName(fileName))
//                .map(postImage -> postImage.setPost(post))
//                .collect(Collectors.toList()); // Собираем в список
//
//        postImageRepository.saveAll(postImageList); // Пакетное сохранение всех объектов
//
//        post.setImages(postImageList);
//        postRepository.save(post);
//    }


}
