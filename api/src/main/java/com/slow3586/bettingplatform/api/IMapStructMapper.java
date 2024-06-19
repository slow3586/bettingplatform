package com.slow3586.bettingplatform.api;

public interface IMapStructMapper<DTO, ENT> {
    DTO toDto(ENT entity);

    ENT toEntity(DTO dto);
}
