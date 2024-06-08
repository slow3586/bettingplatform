package com.slow3586.bettingplaftorm.betservice.bet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BetRepository extends CrudRepository<BetEntity, UUID> {
    List<BetEntity> findAll();
}
