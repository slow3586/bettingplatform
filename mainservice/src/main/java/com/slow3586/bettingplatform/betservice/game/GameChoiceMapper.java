package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.DefaultMapStructConfig;
import com.slow3586.bettingplatform.api.mainservice.GameChoiceDto;
import org.mapstruct.Mapper;

@Mapper(config = DefaultMapStructConfig.class)
public interface GameChoiceMapper {
    GameChoiceDto toDto(GameChoiceEntity entity);

    GameChoiceEntity toEntity(GameChoiceDto dto);
}
