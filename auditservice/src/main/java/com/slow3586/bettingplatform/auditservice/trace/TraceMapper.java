package com.slow3586.bettingplatform.auditservice.trace;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.auditservice.TraceDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface TraceMapper {
    TraceDto toDto(TraceEntity entity);

    TraceEntity toEntity(TraceDto dto);
}
