package com.slow3586.bettingplaftorm.betservice;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BetRepository extends R2dbcRepository<BetEntity, UUID> {
}
