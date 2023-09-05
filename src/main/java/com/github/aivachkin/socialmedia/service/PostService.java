package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.domain.JwtUser;
import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.dto.post.PostDTO;
import com.github.aivachkin.socialmedia.entity.Post;
import com.github.aivachkin.socialmedia.mapper.PostMapper;
import com.github.aivachkin.socialmedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Сервис для работы с постами пользователей
 */
@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final PostMapper postMapper;


    public CreatePostResponse createPost(CreatePostRequest createPostRequest
//            , List<MultipartFile> files
    ) {

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Post post = new Post()
                .setText(createPostRequest.getText())
                .setTitle(createPostRequest.getTitle())
                .setUserId(jwtUser.getUserId())
                .setCreatedAt(LocalDateTime.now());

        postRepository.save(post);

//        if (!CollectionUtils.isEmpty(files)) {
//            imageFileAction.saveFiles(files, post);
//        }
        return postMapper.toCreatePostResponse(post);
    }

    public Page<PostDTO> getPostsByUserId(Long userId, int offset, int limit) {

        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
        Page<Post> postsPage = postRepository.findByUserId(userId, pageable);

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return postsPage.map(post -> postMapper.toPostDto(post, jwtUser.getUsername()));

    }
}
