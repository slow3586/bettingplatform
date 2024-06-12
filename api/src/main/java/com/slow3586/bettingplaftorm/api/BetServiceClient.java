package com.slow3586.bettingplaftorm.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(
    value = "bet",
    url = "${app.client.bet}")
public interface BetServiceClient {
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    UUID make(BetDto betDto);
}
