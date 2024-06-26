package com.slow3586.bettingplatform.userservice.payment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, UUID> {
    List<PaymentEntity> findAllByUserId(UUID userId);
}
