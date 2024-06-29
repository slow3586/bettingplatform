package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.INameRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends INameRepository<CustomerEntity> {
}
