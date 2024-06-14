package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.mainservice.BetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BetMapper {
    BetDto toDto(BetEntity entity);

    BetEntity toEntity(BetDto dto);
}
