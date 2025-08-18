package com.saym.eventory.store.application;

import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.UserType;
import com.saym.eventory.member.domain.repository.MemberRepository;
import com.saym.eventory.store.api.dto.request.CouponRequestDto;
import com.saym.eventory.store.api.dto.response.CouponResponseDto;
import com.saym.eventory.store.domain.Coupon;
import com.saym.eventory.store.domain.CouponType;
import com.saym.eventory.store.domain.Store;
import com.saym.eventory.store.domain.repository.CouponRepository;
import com.saym.eventory.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    // 쿠폰 생성
    public CouponResponseDto createCoupon(Principal principal, Long storeId, CouponRequestDto requestDto) {
        Long ownerId = Long.parseLong(principal.getName());
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND, Error.STORE_NOT_FOUND.getMessage()));
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, Error.MEMBER_NOT_FOUND.getMessage()));

        // 가게 소유자만 쿠폰 생성 가능
        if (!store.getOwner().getId().equals(ownerId) || owner.getUserType() != UserType.OWNER) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "쿠폰 생성 권한이 없습니다.");
        }

        // 쿠폰 타입에 따른 유효성 검사 (AMOUNT_DISCOUNT인데 discountAmount가 null이면 안됨)
        if (requestDto.couponType() == CouponType.AMOUNT_DISCOUNT && requestDto.discountAmount() == null) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, "금액 할인 쿠폰은 할인 금액이 필수입니다.");
        }
        if (requestDto.couponType() == CouponType.ITEM_DISCOUNT && (requestDto.discountItem() == null || requestDto.discountItem().isEmpty())) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, "품목 할인 쿠폰은 할인 품목이 필수입니다.");
        }

        Coupon coupon = Coupon.builder()
                .store(store)
                .couponType(requestDto.couponType())
                .name(requestDto.name())
                .discountAmount(requestDto.discountAmount())
                .discountItem(requestDto.discountItem())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .build();
        couponRepository.save(coupon);

        return CouponResponseDto.from(coupon);
    }

    // 특정 가게의 모든 쿠폰 조회
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getCouponsByStoreId(Long storeId) {
        // 가게 존재 여부 확인
        if (!storeRepository.existsById(storeId)) {
             throw new CustomException(Error.STORE_NOT_FOUND, Error.STORE_NOT_FOUND.getMessage());
         }

        List<Coupon> coupons = couponRepository.findByStoreId(storeId);
        return coupons.stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());
    }

    // 쿠폰 수정
    public CouponResponseDto updateCoupon(Principal principal, Long couponId, CouponRequestDto requestDto) {
        Long ownerId = Long.parseLong(principal.getName());
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY, "쿠폰을 찾을 수 없습니다.")); // 적절한 Error 코드 필요

        if (!coupon.getStore().getOwner().getId().equals(ownerId)) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "쿠폰 수정 권한이 없습니다.");
        }

        if (requestDto.couponType() == CouponType.AMOUNT_DISCOUNT && requestDto.discountAmount() == null) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, "금액 할인 쿠폰은 할인 금액이 필수입니다.");
        }
        if (requestDto.couponType() == CouponType.ITEM_DISCOUNT && (requestDto.discountItem() == null || requestDto.discountItem().isEmpty())) {
            throw new CustomException(Error.BAD_REQUEST_VALIDATION, "품목 할인 쿠폰은 할인 품목이 필수입니다.");
        }

        coupon.updateCoupon(
                requestDto.name(),
                requestDto.discountAmount(),
                requestDto.discountItem(),
                requestDto.startDate(),
                requestDto.endDate()
        );
        return CouponResponseDto.from(coupon);
    }

    // 쿠폰 삭제
    public void deleteCoupon(Principal principal, Long couponId) {
        Long ownerId = Long.parseLong(principal.getName());
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(Error.NOT_FOUND_COMMUNITY, "쿠폰을 찾을 수 없습니다.")); // 적절한 Error 코드 필요

        // 쿠폰 소유자(가게 소유자)만 삭제 가능
        if (!coupon.getStore().getOwner().getId().equals(ownerId)) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "쿠폰 삭제 권한이 없습니다.");
        }
        couponRepository.delete(coupon);
    }
}