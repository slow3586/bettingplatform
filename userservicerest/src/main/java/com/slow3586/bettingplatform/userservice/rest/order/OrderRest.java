package com.slow3586.bettingplatform.userservice.rest.order;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.kafka.KafkaRestUtils;
import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("order")
public class OrderRest {
    ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public Mono<Object> make(@RequestBody OrderRequest orderRequest) {
        return SecurityUtils.getReactivePrincipalId()
            .flatMap(login -> this.sendAndReceive(login, orderRequest));
    }

    protected Mono<Object> sendAndReceive(String key, Object object) {
        return KafkaRestUtils.sendAndReceive(
            replyingKafkaTemplate,
            "order.request",
            key,
            object);
    }
}
