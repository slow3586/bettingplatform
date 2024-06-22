package com.slow3586.bettingplatform.auditservice.metric;

import com.slow3586.bettingplatform.api.auditservice.MetricDto;
import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface MetricMapper {
    MetricDto toDto(MetricEntity entity);

    MetricEntity toEntity(MetricDto dto);
}
