package com.slow3586.bettingplaftorm.betservice.price;

import com.slow3586.bettingplaftorm.api.PriceEntityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceEntityMapper {
    PriceEntityDto toDto(PriceEntity userEntity);

    PriceEntity toEntity(PriceEntityDto user);
}
