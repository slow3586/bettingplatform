package com.slow3586.bettingplatform.api.mainservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(
    value = "price",
    url = "${app.client.main}/price")
public interface PriceServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/latest")
    List<PriceDto> getLatest();
}
