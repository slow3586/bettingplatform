package com.slow3586.bettingplatform.api;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface INameRepository<T> extends CrudRepository<T, UUID> {
    Optional<T> findByName(String name);
    boolean existsByName(String name);
}
