package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.mainservice.dto.BetDto;
import com.slow3586.bettingplatform.api.mainservice.dto.BetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("bet")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class BetRest {
    BetService betService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public List<BetDto> getByCurrentUser() {
        return this.getByUser(SecurityUtils.getPrincipalId());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public List<BetDto> getByUser(@PathVariable("userId") String login) {
        return betService.getByUser(login);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public void register(BetRequest betRequest) {
        kafkaTemplate.send("bet.register",
            SecurityUtils.getPrincipalId(),
            betRequest);
    }
}
