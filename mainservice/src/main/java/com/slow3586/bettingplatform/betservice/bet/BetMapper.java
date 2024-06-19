package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.IMapStructMapperWithRequest;
import com.slow3586.bettingplatform.api.mainservice.BetDto;
import com.slow3586.bettingplatform.api.mainservice.BetRequest;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface BetMapper extends IMapStructMapperWithRequest<BetDto, BetEntity, BetRequest> {
}
