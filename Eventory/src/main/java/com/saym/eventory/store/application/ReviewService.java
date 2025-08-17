package com.saym.eventory.store.application;

import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.store.api.dto.response.ReviewResponseDto;
import com.saym.eventory.store.domain.Review;
import com.saym.eventory.store.domain.repository.ReviewRepository;
import com.saym.eventory.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    public List<ReviewResponseDto> getReviewsByStoreId(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new CustomException(Error.STORE_NOT_FOUND, Error.STORE_NOT_FOUND.getMessage());
        }

        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        if (reviews.isEmpty()) {
            throw new CustomException(Error.REVIEW_NOT_FOUND, Error.REVIEW_NOT_FOUND.getMessage());
        }

        return reviews.stream()
                .map(review -> new ReviewResponseDto(review.getId(), review.getContent()))
                .collect(Collectors.toList());
    }
}