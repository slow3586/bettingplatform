package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.mainservice.GameChoiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GameChoiceMapper {
    GameChoiceDto toDto(GameChoiceEntity entity);

    GameChoiceEntity toEntity(GameChoiceDto dto);
}
