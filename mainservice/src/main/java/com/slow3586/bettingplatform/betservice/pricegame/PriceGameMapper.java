package com.slow3586.bettingplatform.betservice.pricegame;

import com.slow3586.bettingplatform.api.mainservice.dto.GameTypeDto;
import com.slow3586.bettingplatform.api.mainservice.dto.PriceGameDto;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import com.slow3586.bettingplatform.betservice.gametype.GameTypeEntity;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface PriceGameMapper extends IMapStructMapper<PriceGameDto, PriceGameEntity> {
}
