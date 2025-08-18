package com.saym.eventory.store.domain.repository;

import com.saym.eventory.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);

    @Query("SELECT s FROM Store s WHERE s.address LIKE %:areaName%")
    List<Store> findByAddressContains(@Param("areaName") String areaName);
}