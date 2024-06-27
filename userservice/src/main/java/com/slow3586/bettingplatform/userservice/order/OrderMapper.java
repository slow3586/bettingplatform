package com.slow3586.bettingplatform.userservice.order;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import com.slow3586.bettingplatform.api.userservice.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface OrderMapper extends IMapStructMapper<OrderDto, OrderEntity> {
}
