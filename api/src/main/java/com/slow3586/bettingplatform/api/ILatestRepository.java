package com.slow3586.bettingplatform.api;

import org.springframework.data.repository.CrudRepository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ILatestRepository<T> extends CrudRepository<T, UUID> {
    List<T> findLatest1Min(String name);
    List<T> findLatest5Min(String name);
    List<T> findLatest1Hour(String name);
    List<T> findLatest1Day(String name);
    List<T> findLatest(String name, Duration duration);
}
