package com.slow3586.bettingplatform.userservice.product;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("product")
public class ProductEntity {
    @Id
    UUID id;
    String name;
    double price;
}
