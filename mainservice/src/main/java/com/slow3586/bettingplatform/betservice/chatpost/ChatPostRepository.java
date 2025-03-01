package com.slow3586.bettingplatform.betservice.chatpost;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatPostRepository extends CrudRepository<ChatPostEntity, UUID> {
    @Query("SELECT * from chat_post ORDER BY created_at DESC LIMIT 10")
    List<ChatPostEntity> findLatest();
}
