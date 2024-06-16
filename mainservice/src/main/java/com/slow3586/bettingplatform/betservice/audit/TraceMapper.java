package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.TraceDto;
import com.slow3586.bettingplatform.api.mainservice.BetDto;
import com.slow3586.bettingplatform.betservice.bet.BetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TraceMapper {
    TraceDto toDto(TraceEntity entity);

    TraceEntity toEntity(TraceDto dto);
}
