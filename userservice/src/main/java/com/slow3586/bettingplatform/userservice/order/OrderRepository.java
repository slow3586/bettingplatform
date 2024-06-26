package com.slow3586.bettingplatform.userservice.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<OrderEntity, UUID> {
    List<OrderEntity> findAllByUserId(UUID userId);
}
