package com.slow3586.bettingplaftorm.betservice.game;

import com.slow3586.bettingplaftorm.api.GameEntityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameEntityMapper {
    GameEntityDto toDto(GameEntity userEntity);

    GameEntity toEntity(GameEntityDto user);
}
