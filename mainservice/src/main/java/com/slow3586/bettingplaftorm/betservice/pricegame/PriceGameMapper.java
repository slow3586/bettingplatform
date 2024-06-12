package com.slow3586.bettingplaftorm.betservice.pricegame;

import com.slow3586.bettingplaftorm.api.PriceGameDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceGameMapper {
    PriceGameDto toDto(PriceGameEntity entity);

    PriceGameEntity toEntity(PriceGameDto dto);
}
