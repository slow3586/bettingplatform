package com.slow3586.bettingplatform.userservice.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface AuthRepository extends CrudRepository<AuthEntity, UUID> {
    boolean existsByLogin(String login);
}
