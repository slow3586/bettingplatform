package com.slow3586.bettingplatform.userservice.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
