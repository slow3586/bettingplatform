package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.IMapStructMapperWithRequest;
import com.slow3586.bettingplatform.api.mainservice.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.ChatPostRequest;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface ChatPostMapper extends IMapStructMapperWithRequest<ChatPostDto, ChatPostEntity, ChatPostRequest> {
}
