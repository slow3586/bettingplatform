package com.slow3586.bettingplatform.betservice.price;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import com.slow3586.bettingplatform.api.mainservice.PriceDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface PriceMapper extends IMapStructMapper<PriceDto, PriceEntity> {
}
