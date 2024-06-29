package com.slow3586.bettingplatform.api.userservice.client;

import com.slow3586.bettingplatform.api.userservice.dto.AuthLoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.AuthRegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(
    value = "auth",
    url = "${app.client.user}/auth")
public interface AuthServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    String login(@RequestBody AuthLoginRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    String register(@RequestBody AuthRegisterRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/token")
    UUID token(String token);
}
