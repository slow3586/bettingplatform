package com.slow3586.bettingplaftorm.betservice.game;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameEntityMapper {
    GameEntityDto toDto(GameEntity userEntity);

    GameEntity toEntity(GameEntityDto user);
}
