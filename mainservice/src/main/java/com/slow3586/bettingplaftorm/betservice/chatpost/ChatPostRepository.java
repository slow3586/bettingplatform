package com.slow3586.bettingplaftorm.betservice.chatpost;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatPostRepository extends CrudRepository<ChatPostEntity, UUID> {
}
