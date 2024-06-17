package com.slow3586.bettingplatform.auditservice.metric;

import com.slow3586.bettingplatform.api.auditservice.MetricDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MetricMapper {
    MetricDto toDto(MetricEntity entity);

    MetricEntity toEntity(MetricDto dto);
}
