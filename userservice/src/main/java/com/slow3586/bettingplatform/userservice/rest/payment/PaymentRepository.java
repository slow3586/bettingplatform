package com.slow3586.bettingplatform.userservice.rest.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface PaymentRepository extends CrudRepository<PaymentEntity, UUID> {
}
