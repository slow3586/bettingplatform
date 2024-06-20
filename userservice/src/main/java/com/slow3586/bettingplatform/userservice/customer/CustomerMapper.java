package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface CustomerMapper extends IMapStructMapper<CustomerDto, CustomerEntity> {
}
