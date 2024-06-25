package com.slow3586.bettingplatform.userservice.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("order")
public class OrderRest {
    OrderService orderService;

    /*@PostMapping
    public Mono<UUID> make(OrderRequest orderRequest) {
        return orderService.make(orderRequest);
    }*/
}
