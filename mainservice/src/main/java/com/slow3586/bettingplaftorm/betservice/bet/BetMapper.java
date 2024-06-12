package com.slow3586.bettingplaftorm.betservice.bet;

import com.slow3586.bettingplaftorm.api.BetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BetMapper {
    BetDto toDto(BetEntity entity);

    BetEntity toEntity(BetDto dto);
}
