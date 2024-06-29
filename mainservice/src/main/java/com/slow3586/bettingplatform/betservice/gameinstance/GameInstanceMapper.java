package com.slow3586.bettingplatform.betservice.gameinstance;

import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceDto;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface GameInstanceMapper extends IMapStructMapper<GameInstanceDto, GameInstanceEntity> {
}
