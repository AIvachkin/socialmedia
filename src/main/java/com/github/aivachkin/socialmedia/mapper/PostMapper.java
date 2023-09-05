package com.github.aivachkin.socialmedia.mapper;

import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.dto.post.PostDTO;
import com.github.aivachkin.socialmedia.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    CreatePostResponse toCreatePostResponse(Post post);

    //    @Mapping(source = "images", target = "imageLinks")
    default PostDTO toPostDto(Post post, String username) {

        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setText(post.getText());
        postDTO.setUsername(username);
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUserId(post.getUserId());


        return postDTO;
    }

}
