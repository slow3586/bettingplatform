package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.IMapStructMapper;
import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface CustomerMapper extends IMapStructMapper<CustomerDto, CustomerEntity> {
}
