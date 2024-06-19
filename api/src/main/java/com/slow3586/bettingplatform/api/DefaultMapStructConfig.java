package com.slow3586.bettingplatform.api;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueCheckStrategy;

@MapperConfig(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public class DefaultMapStructConfig{
}
