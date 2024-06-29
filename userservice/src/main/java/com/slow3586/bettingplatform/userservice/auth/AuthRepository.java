package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.INameRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AuthRepository extends INameRepository<AuthEntity> {
}
