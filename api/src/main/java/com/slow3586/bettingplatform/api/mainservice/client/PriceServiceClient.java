package com.slow3586.bettingplatform.api.mainservice.client;

import com.slow3586.bettingplatform.api.mainservice.dto.PriceGameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(
    value = "price",
    url = "${app.client.main}/price")
public interface PriceServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/latest")
    List<PriceGameDto> getLatest();
}
