package com.slow3586.bettingplaftorm.betservice.price;

import com.slow3586.bettingplaftorm.betservice.game.GameEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, UUID> {
    @Query("SELECT value from prices WHERE time < CURRENT_TIMESTAMP - interval '1 minute'")
    List<String> findLatest();
}
