package com.slow3586.bettingplatform.userservice.customer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("customer")
public class CustomerEntity {
    @Id
    UUID id;
    UUID userId;
    String name;
    double balance;
}
