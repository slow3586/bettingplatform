package com.slow3586.bettingplatform.userservice.order;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("order")
public class OrderRest {
    OrderService orderService;
    OrderMapper orderMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public void process(@RequestBody OrderRequest orderRequest) {
        kafkaTemplate.send("order.process",
            SecurityUtils.getPrincipalId(),
            orderRequest);
    }
}
