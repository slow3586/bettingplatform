package com.slow3586.bettingplatform.api.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(
    value = "customer",
    url = "${app.client.user}/customer")
public interface CustomerServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "")
    String getCurrent();

    @RequestMapping(method = RequestMethod.GET, value = "/private/{uuid}")
    String getPrivateByUser(@PathVariable("uuid") UUID uuid);

    @RequestMapping(method = RequestMethod.GET, value = "/public/{uuid}")
    UUID getPublicByUser(@PathVariable("uuid") UUID uuid);
}
