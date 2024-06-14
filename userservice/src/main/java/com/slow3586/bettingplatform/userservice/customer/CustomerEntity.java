package com.slow3586.bettingplatform.userservice.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("customer")
public class CustomerEntity implements Persistable<UUID> {
    @Id
    UUID id;
    String name;
    String email;
    double balance;
    @Transient
    @JsonIgnore
    boolean isNew;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
