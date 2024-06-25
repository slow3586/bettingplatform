package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.mainservice.dto.BetDto;
import com.slow3586.bettingplatform.api.mainservice.dto.BetRequest;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapperWithRequest;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface BetMapper extends IMapStructMapperWithRequest<BetDto, BetEntity, BetRequest> {
}
