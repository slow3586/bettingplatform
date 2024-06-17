package com.slow3586.bettingplatform.auditservice.trace;

import com.slow3586.bettingplatform.api.auditservice.TraceDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TraceMapper {
    TraceDto toDto(TraceEntity entity);

    TraceEntity toEntity(TraceDto dto);
}
