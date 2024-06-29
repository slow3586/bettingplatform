package com.slow3586.bettingplatform.betservice.gamestat;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface GameStatMapper extends IMapStructMapper<GamesS, GameStatEntity> {
}
