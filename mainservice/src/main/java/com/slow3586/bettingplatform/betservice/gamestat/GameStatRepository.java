package com.slow3586.bettingplatform.betservice.gamestat;

import com.slow3586.bettingplatform.api.INameRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameStatRepository extends INameRepository<GameStatEntity> {
    @Query("SELECT time, value from gamestat WHERE instrument = :instrument AND time > CURRENT_TIMESTAMP - interval '1 minute'")
    List<GameStatEntity> findLatest(@Param("instrument") String instrument);
}
