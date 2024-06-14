package com.slow3586.bettingplatform.api.mainservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(
    value = "bet",
    url = "${app.client.main}/bet")
public interface BetServiceClient {
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    UUID make(BetDto betDto);
}
