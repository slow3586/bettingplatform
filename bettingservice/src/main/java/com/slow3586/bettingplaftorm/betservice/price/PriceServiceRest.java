package com.slow3586.bettingplaftorm.betservice.price;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("price")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceServiceRest {
    PriceService priceService;

    @GetMapping("latest")
    public List<String> getLatest() {
        return priceService.getLatest();
    }
}
