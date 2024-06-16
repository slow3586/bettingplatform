package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerMapper {
    CustomerDto toDto(CustomerEntity entity);

    CustomerEntity toEntity(CustomerDto dto);
}
