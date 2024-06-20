package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import com.slow3586.bettingplatform.api.mainservice.GameDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface GameMapper extends IMapStructMapper<GameDto, GameEntity> {
}
