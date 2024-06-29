package com.slow3586.bettingplatform.betservice.pricegame;

import com.slow3586.bettingplatform.api.mainservice.dto.PriceGameDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceGameService {
    PriceGameRepository priceGameRepository;
    PriceGameMapper priceGameMapper;
    KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${priceUrl}")
    String priceUrl;
    LeaderLatch leaderLatch;
    RestTemplate restTemplate;

    @Scheduled(fixedRate = 1000)
    public void updateGame() {
        if (!leaderLatch.hasLeadership()) {
            return;
        }

        final Instant now = Instant.now();

        final PriceGameEntity priceGameEntity = priceGameRepository
            .findByName("BTC")
            .orElseThrow();

        if (now.isAfter(priceGameEntity.getFinishAt())) {
            priceGameEntity.setFinished(true);
        }

        try {
            final ResponseEntity<String> responseEntity =
                new RestTemplate().getForEntity(priceGameEntity.getPriceUrl(), String.class);

            final String body = responseEntity.getBody();
            if (StringUtils.isBlank(body)) {
                throw new IllegalStateException("Body is empty");
            }

            final double price;
            try {
                final Pattern pattern = Pattern.compile(priceGameEntity.getPricePattern());
                final Matcher matcher = pattern.matcher(body);
                if (!matcher.matches()) {
                    throw new IllegalArgumentException("Price not found");
                }

                price = Double.parseDouble(matcher.group(1));
            } catch (Exception e) {
                throw new IllegalStateException("Could not parse price: " + body);
            }

            priceGameEntity.setPrice(price);
            priceGameEntity.setPriceUpdatedAt(now);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        priceGameRepository.save(priceGameEntity);
    }
}
