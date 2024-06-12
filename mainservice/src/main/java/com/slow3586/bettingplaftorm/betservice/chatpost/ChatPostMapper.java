package com.slow3586.bettingplaftorm.betservice.chatpost;

import com.slow3586.bettingplaftorm.api.ChatPostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatPostMapper {
    ChatPostDto toDto(ChatPostEntity entity);

    ChatPostEntity toEntity(ChatPostDto dto);
}
