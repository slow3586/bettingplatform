package com.slow3586.bettingplaftorm.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(
    value = "user",
    url = "${app.client.user}")
public interface UserServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    String login(@RequestBody LoginRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    String register(@RequestBody RegisterRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/token")
    UUID token(String token);
}
