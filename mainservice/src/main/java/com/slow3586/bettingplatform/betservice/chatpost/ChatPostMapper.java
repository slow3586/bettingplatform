package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostRequest;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapperWithRequest;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface ChatPostMapper extends IMapStructMapperWithRequest<ChatPostDto, ChatPostEntity, ChatPostRequest> {
}
