package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.mainservice.GameDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GameMapper {
    GameDto toDto(GameEntity entity);

    GameEntity toEntity(GameDto dto);
}
