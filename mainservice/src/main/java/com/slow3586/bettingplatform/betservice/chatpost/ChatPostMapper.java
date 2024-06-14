package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.mainservice.ChatPostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatPostMapper {
    ChatPostDto toDto(ChatPostEntity entity);

    ChatPostEntity toEntity(ChatPostDto dto);
}
