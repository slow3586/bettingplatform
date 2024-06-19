package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.mainservice.BetDto;
import com.slow3586.bettingplatform.api.mainservice.BetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BetMapper {
    BetDto toDto(BetEntity entity);

    BetEntity toEntity(BetDto dto);

    BetDto toDto(BetRequest betRequest);
}
