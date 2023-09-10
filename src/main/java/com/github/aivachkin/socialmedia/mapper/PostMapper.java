package com.github.aivachkin.socialmedia.mapper;

import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.dto.post.PostDto;
import com.github.aivachkin.socialmedia.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {


    CreatePostResponse toCreatePostResponse(Post post);

    default PostDto toPostDto(Post post, Long userId) {

        PostDto postDTO = new PostDto();

        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setText(post.getText());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUserId(userId);

        if (post.getImage()!=null){
            postDTO.setImageLink(post.getImage().getImage());
        }

        return postDTO;
    }

    default PostDto toPostTargetUserDto(Post post) {

        PostDto postDTO = new PostDto();

        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setText(post.getText());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUserId(post.getUserId());

        if (post.getImage()!=null){
            postDTO.setImageLink(post.getImage().getImage());
        }

        return postDTO;
    }

}
