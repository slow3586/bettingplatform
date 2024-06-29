package com.slow3586.bettingplatform.userservice.product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductEntity get(UUID uuid) {
        return productRepository.findById(uuid)
            .orElseThrow(() -> new IllegalArgumentException("Unknown product"));
    }
}
