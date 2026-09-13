package com.securebank.api.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolverBucket(String ip) {
        return cache.computeIfAbsent(ip, this::criarNovoBucket);
    }

    private Bucket criarNovoBucket(String ip) {

        Refill recarga = Refill.intervally(5, Duration.ofMinutes(1));
        Bandwidth limite = Bandwidth.classic(5, recarga);
        return Bucket.builder().addLimit(limite).build();
    }
}
