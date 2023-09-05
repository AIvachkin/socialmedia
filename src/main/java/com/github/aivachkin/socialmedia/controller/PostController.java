package com.github.aivachkin.socialmedia.controller;

import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Создание поста
//    @Operation(summary = "Создать пост", description = "Пользователи могут создавать новые посты, указывая текст, заголовок и прикрепляя изображения.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Пост успешно опубликован"),
//            @ApiResponse(responseCode = "404", description = "Пользователь не существует")
//    })
    @PostMapping(
//            value = "/posts"
//            , consumes = "multipart/form-data"
    )
    public ResponseEntity<CreatePostResponse> createPost(
            @Valid
            @RequestBody CreatePostRequest createPostRequest)
//
//            @ModelAttribute("createPostDTO") CreatePostDTO createPostDTO,
//            @RequestParam(name = "files", required = false) List<MultipartFile> files)
    {

        return ResponseEntity.ok(postService.createPost(createPostRequest));
    }


}
