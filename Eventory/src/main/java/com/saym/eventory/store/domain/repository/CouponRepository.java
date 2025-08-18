package com.saym.eventory.store.domain.repository;

import com.saym.eventory.store.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByStoreId(Long storeId);
    void deleteByStoreId(Long storeId);
}