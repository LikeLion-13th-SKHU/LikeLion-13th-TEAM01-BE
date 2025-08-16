package com.saym.eventory.shop.domain.repository;

import com.saym.eventory.shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT s FROM Shop s " +
            "LEFT JOIN FETCH s.menus m " +
            "LEFT JOIN FETCH s.coupons c " +
            "LEFT JOIN FETCH s.reviews r " +
            "WHERE s.shopId = :shopId")
    Optional<Shop> findShopDetailById(@Param("shopId") Long shopId);

}
