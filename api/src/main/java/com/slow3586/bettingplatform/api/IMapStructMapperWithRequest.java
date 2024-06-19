package com.slow3586.bettingplatform.api;

public interface IMapStructMapperWithRequest<DTO, ENT, REQ> {
    DTO toDto(ENT entity);

    ENT toEntity(DTO dto);

    DTO requestToDto(REQ request);
}
