package com.saym.eventory.store.domain.repository;

import com.saym.eventory.store.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Long storeId);
}
