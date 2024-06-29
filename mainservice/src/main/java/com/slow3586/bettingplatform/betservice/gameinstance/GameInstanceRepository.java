package com.slow3586.bettingplatform.betservice.gameinstance;

import com.slow3586.bettingplatform.api.INameRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameInstanceRepository extends INameRepository<GameInstanceEntity> {
    @Query("SELECT * from game WHERE status <> 'finished'")
    List<GameInstanceEntity> findUnfinished();

    @Query("SELECT * from game WHERE finish_at < CURRENT_TIMESTAMP AND status <> 'finished'")
    List<GameInstanceEntity> findReadyToBeFinished();
}
