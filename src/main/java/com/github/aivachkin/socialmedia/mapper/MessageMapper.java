package com.github.aivachkin.socialmedia.mapper;

import com.github.aivachkin.socialmedia.dto.message.MessageResponseDto;
import com.github.aivachkin.socialmedia.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    MessageResponseDto toMessageResponseDto (Message message);
}
