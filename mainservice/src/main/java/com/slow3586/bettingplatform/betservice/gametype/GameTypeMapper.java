package com.slow3586.bettingplatform.betservice.gametype;

import com.slow3586.bettingplatform.api.mainservice.dto.GameTypeDto;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface GameTypeMapper extends IMapStructMapper<GameTypeDto, GameTypeEntity> {
}
