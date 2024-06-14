package com.slow3586.bettingplatform.betservice.chatpost;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatPostRepository extends CrudRepository<ChatPostEntity, UUID> {
    @Query("SELECT * from chat_post WHERE created_at ")
    List<ChatPostEntity> findLatest();
}
