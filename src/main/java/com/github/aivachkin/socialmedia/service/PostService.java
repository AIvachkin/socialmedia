package com.github.aivachkin.socialmedia.service;

import com.github.aivachkin.socialmedia.domain.JwtUser;
import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.entity.Post;
import com.github.aivachkin.socialmedia.filter.JwtFilter;
import com.github.aivachkin.socialmedia.mapper.PostMapper;
import com.github.aivachkin.socialmedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Сервис для работы с постами пользователей
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtFilter jwtFilter;

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    public CreatePostResponse createPost(CreatePostRequest createPostRequest
//            , List<MultipartFile> files
    ) {

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


//        final String token = jwtFilter.getTokenFromRequest(createPostRequest);
//        if (token != null && jwtProvider.validateAccessToken(token)) {
//            final Claims claims = jwtProvider.getAccessClaims(token);
//            final JwtAuthentication jwtInfoToken = JwtUtility.generate(claims);
//            jwtInfoToken.setAuthenticated(true);
//            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
//        }

//        User user = userRepository.findById(getAuthenticatedUserId()).orElseThrow(UserNotFoundException::new);
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
}
