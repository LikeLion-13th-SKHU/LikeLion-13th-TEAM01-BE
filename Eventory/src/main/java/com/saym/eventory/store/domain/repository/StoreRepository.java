package com.saym.eventory.store.domain.repository;

import com.saym.eventory.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);
}