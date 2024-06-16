package com.slow3586.bettingplatform.betservice.price;

import com.slow3586.bettingplatform.api.mainservice.PriceDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PriceMapper {
    PriceDto toDto(PriceEntity entity);

    PriceEntity toEntity(PriceDto dto);
}
