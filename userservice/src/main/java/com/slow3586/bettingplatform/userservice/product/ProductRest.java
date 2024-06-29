package com.slow3586.bettingplatform.userservice.product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("order")
public class ProductRest {
    ProductService productService;

    @GetMapping("/{uuid}")
    public ProductEntity get(@PathVariable("uuid") UUID uuid) {
        return productService.get(uuid);
    }
}
