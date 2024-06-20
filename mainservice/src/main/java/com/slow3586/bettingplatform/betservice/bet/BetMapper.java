package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapperWithRequest;
import com.slow3586.bettingplatform.api.mainservice.BetDto;
import com.slow3586.bettingplatform.api.mainservice.BetRequest;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface BetMapper extends IMapStructMapperWithRequest<BetDto, BetEntity, BetRequest> {
}
