package com.slow3586.bettingplaftorm.betservice.price;

import com.slow3586.bettingplaftorm.api.PriceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    PriceDto toDto(PriceEntity entity);

    PriceEntity toEntity(PriceDto dto);
}
