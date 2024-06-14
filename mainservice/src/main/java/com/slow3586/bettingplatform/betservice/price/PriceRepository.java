package com.slow3586.bettingplatform.betservice.price;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, UUID> {
    @Query("SELECT * from price WHERE time < CURRENT_TIMESTAMP - interval '1 minute'")
    List<PriceEntity> findLatest();
}
