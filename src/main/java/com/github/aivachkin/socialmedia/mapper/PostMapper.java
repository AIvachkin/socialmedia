package com.github.aivachkin.socialmedia.mapper;

import com.github.aivachkin.socialmedia.dto.post.CreatePostResponse;
import com.github.aivachkin.socialmedia.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    CreatePostResponse toCreatePostResponse (Post post);
}
