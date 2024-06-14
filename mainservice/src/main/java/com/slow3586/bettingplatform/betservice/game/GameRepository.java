package com.slow3586.bettingplatform.betservice.game;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    @Query("SELECT * from game WHERE status <> 'finished'")
    List<GameEntity> findUnfinished();

    @Query("SELECT * from game WHERE finish_at < CURRENT_TIMESTAMP AND status <> 'finished'")
    List<GameEntity> findReadyToBeFinished();
}
