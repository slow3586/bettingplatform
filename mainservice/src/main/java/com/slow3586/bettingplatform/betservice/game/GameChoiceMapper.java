package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.mapstruct.IMapStructConfig;
import com.slow3586.bettingplatform.api.mainservice.GameChoiceDto;
import org.mapstruct.Mapper;

@Mapper(config = IMapStructConfig.class)
public interface GameChoiceMapper {
    GameChoiceDto toDto(GameChoiceEntity entity);

    GameChoiceEntity toEntity(GameChoiceDto dto);
}
