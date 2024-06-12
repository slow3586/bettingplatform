package com.slow3586.bettingplaftorm.betservice.pricegame;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceGameRepository extends CrudRepository<PriceGameEntity, UUID> {
    @Query("SELECT * from price_game WHERE date_finished < CURRENT_TIMESTAMP AND is_finished = false")
    List<PriceGameEntity> findReadyToBeFinished();
}
