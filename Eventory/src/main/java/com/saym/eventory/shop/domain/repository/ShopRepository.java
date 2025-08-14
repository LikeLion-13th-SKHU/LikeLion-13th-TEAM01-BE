package com.saym.eventory.shop.domain.repository;

import com.saym.eventory.shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
