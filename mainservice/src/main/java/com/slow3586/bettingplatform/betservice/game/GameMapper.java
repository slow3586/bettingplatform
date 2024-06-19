package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.IMapStructMapper;
import com.slow3586.bettingplatform.api.mainservice.GameDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface GameMapper extends IMapStructMapper<GameDto, GameEntity> {
}
