package com.slow3586.bettingplatform.userservice.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<CustomerEntity, UUID> {
}
