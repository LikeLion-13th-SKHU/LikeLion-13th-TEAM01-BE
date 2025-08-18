package com.saym.eventory.store.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.store.api.dto.request.CouponRequestDto;
import com.saym.eventory.store.api.dto.response.CouponResponseDto;
import com.saym.eventory.store.application.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon API", description = "쿠폰 관련 API 입니다. (가맹점주 모드)")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 생성 (가게 소유자만 가능)")
    @PostMapping("/store/{storeId}")
    public RspTemplate<CouponResponseDto> createCoupon(
            Principal principal,
            @PathVariable Long storeId,
            @RequestBody CouponRequestDto requestDto) {
        return RspTemplate.ok(couponService.createCoupon(principal, storeId, requestDto));
    }

    @Operation(summary = "특정 가게의 모든 쿠폰 조회")
    @GetMapping("/store/{storeId}")
    public RspTemplate<List<CouponResponseDto>> getCouponsByStoreId(@PathVariable Long storeId) {
        return RspTemplate.ok(couponService.getCouponsByStoreId(storeId));
    }

    @Operation(summary = "쿠폰 수정 (쿠폰 소유자만 가능)")
    @PatchMapping("/{couponId}")
    public RspTemplate<CouponResponseDto> updateCoupon(
            Principal principal,
            @PathVariable Long couponId,
            @RequestBody CouponRequestDto requestDto) {
        return RspTemplate.ok(couponService.updateCoupon(principal, couponId, requestDto));
    }

    @Operation(summary = "쿠폰 삭제 (쿠폰 소유자만 가능)")
    @DeleteMapping("/{couponId}")
    public RspTemplate<Void> deleteCoupon(Principal principal, @PathVariable Long couponId) {
        couponService.deleteCoupon(principal, couponId);
        return RspTemplate.ok(null);
    }
}