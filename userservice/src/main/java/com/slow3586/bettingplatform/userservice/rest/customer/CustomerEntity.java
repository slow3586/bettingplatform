package com.slow3586.bettingplatform.userservice.rest.customer;

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
    String login;
    String name;
    double balance;
    String status;
    boolean hasPremium;
}
