package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.IMapStructMapper;
import com.slow3586.bettingplatform.api.userservice.PaymentDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface PaymentMapper extends IMapStructMapper<PaymentDto, PaymentEntity> {
}
