package com.slow3586.bettingplaftorm.betservice.game;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    @Query("SELECT * from games WHERE date_finished < CURRENT_TIMESTAMP AND is_finished = false")
    List<GameEntity> findReadyToBeFinished();
}
