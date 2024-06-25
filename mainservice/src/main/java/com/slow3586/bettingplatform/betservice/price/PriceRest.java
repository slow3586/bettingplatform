package com.slow3586.bettingplatform.betservice.price;

import com.slow3586.bettingplatform.api.mainservice.dto.PriceDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("price")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceRest {
    PriceService priceService;

    @GetMapping("latest/{instrument}")
    public List<PriceDto> getLatest(@PathVariable("instrument") String instrument) {
        return priceService.getLatest(instrument);
    }
}
