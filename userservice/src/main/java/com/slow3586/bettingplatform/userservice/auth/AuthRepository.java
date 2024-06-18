package com.slow3586.bettingplatform.userservice.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthRepository extends CrudRepository<AuthEntity, UUID> {
    void deleteByUserId(UUID userId);
    AuthEntity findByLogin(String login);
    boolean existsByUserId(UUID userId);
    boolean existsByLogin(String login);
}
