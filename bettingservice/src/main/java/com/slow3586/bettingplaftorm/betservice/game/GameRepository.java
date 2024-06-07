package com.slow3586.bettingplaftorm.betservice.game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, UUID> {
    List<GameEntity> findAllByFinishedBeforeAndFinished(Date date, boolean finished);
}
