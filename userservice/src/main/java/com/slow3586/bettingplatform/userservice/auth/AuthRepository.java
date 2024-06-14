package com.slow3586.bettingplatform.userservice.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthRepository extends CrudRepository<AuthEntity, UUID> {
    AuthEntity findByLogin(String login);
}
