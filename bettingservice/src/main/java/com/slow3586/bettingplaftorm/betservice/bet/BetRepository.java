package com.slow3586.bettingplaftorm.betservice.bet;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BetRepository extends CrudRepository<BetEntity, UUID> {
}
