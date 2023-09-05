package com.github.aivachkin.socialmedia.controller;

import com.github.aivachkin.socialmedia.dto.post.CreatePostRequest;
import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.dto.post.PostDTO;
import com.github.aivachkin.socialmedia.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Контроллер для работы с постами пользователей
 */
@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    /**
     * Создание поста пользователем
     *
     * @param createPostRequest ДТО, содержащий необходимую информацию для создания поста
     */
    @Operation(
            summary = "Создание поста",
            description = "Необходимо ввести заголовок, текст (содержание) и прикрепить изображения. " +
                    "Изображения прикреплять необязательно",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пост размещен"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не существует")
            }, tags = "Post")
    @PostMapping()
    public ResponseEntity<CreatePostResponse> createPost(
            @Valid @RequestBody CreatePostRequest createPostRequest
//            @ModelAttribute("createPostResponse") CreatePostResponse createPostResponse,
//            @RequestParam(required = false) MultipartFile image
    )  {

        return ResponseEntity.ok(postService.createPost(createPostRequest));
    }


        @Operation(
            summary = "Установка/обновление картинки поста",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Картинка поста установлена, путь к ней сохранена в БД",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class))
                    )
            }, tags = "Post",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MultipartFile.class))
            )

    )
    @PatchMapping(value = "/image/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePostImage(@PathVariable Long postId,
                                                  @RequestParam MultipartFile image) throws IOException {
        Boolean updatePostImageDone = postService.updatePostImage(postId, image);
        if (updatePostImageDone) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }


    /**
     * Получение всех постов пользователя по его id
     *
     * @param userId id пользователя
     * @param offset номер страницы (необязательный параметр)
     * @param limit  количество постов на странице (необязательный параметр)
     * @return ДТО, содержащий необходимые параметры для отображения
     */
    @Operation(
            summary = "Получение всех постов пользователя",
            description = "Постраничный вывод постов пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список постов пользователя получен"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь с таким id не существует")
            }, tags = "Post")
    @GetMapping("/{userId}")
    public ResponseEntity<Page<PostDTO>> getPostByUserId(
            @Parameter(description = "ID автора поста") @PathVariable Long userId,
            @Parameter(description = "Номер страницы") @RequestParam(required = false, defaultValue = "0") int offset,
            @Parameter(description = "Размер страницы") @RequestParam(required = false, defaultValue = "10") int limit) {

        return ResponseEntity.ok(postService.getPostsByUserId(userId, offset, limit));
    }


}
