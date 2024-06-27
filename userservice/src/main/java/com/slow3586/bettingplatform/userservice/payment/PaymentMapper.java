package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructMapper;
import com.slow3586.bettingplatform.api.userservice.dto.PaymentDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface PaymentMapper extends IMapStructMapper<PaymentDto, PaymentEntity> {
}
