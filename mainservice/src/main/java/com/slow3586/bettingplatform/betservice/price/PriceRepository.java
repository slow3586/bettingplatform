package com.slow3586.bettingplatform.betservice.price;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, UUID> {
    @Query("SELECT time, value from price WHERE instrument = :instrument AND time > CURRENT_TIMESTAMP - interval '1 minute'")
    List<PriceEntity> findLatest(@Param("instrument") String instrument);
}
