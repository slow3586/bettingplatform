package com.slow3586.bettingplatform.auditservice.trace;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.auditservice.TraceDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface TraceMapper {
    TraceDto toDto(TraceEntity entity);

    TraceEntity toEntity(TraceDto dto);
}
