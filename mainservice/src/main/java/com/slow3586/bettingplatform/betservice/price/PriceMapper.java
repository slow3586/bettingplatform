package com.slow3586.bettingplatform.betservice.price;

import com.slow3586.bettingplatform.api.mainservice.PriceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    PriceDto toDto(PriceEntity entity);

    PriceEntity toEntity(PriceDto dto);
}
