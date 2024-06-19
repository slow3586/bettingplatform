package com.slow3586.bettingplatform.auditservice.metric;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.auditservice.MetricDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface MetricMapper {
    MetricDto toDto(MetricEntity entity);

    MetricEntity toEntity(MetricDto dto);
}
