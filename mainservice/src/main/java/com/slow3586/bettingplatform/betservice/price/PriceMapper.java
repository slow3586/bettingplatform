package com.slow3586.bettingplatform.betservice.price;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.IMapStructMapper;
import com.slow3586.bettingplatform.api.mainservice.PriceDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface PriceMapper extends IMapStructMapper<PriceDto, PriceEntity> {
}
