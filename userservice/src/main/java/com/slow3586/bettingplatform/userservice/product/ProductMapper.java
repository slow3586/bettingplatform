package com.slow3586.bettingplatform.userservice.product;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapperWithRequest;
import com.slow3586.bettingplatform.api.userservice.dto.OrderDto;
import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface ProductMapper extends IMapStructMapperWithRequest<OrderDto, ProductEntity, OrderRequest> {
}
