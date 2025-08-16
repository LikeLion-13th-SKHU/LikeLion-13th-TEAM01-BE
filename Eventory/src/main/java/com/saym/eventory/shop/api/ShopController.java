package com.saym.eventory.shop.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.shop.api.dto.ShopRequestDto;
import com.saym.eventory.shop.api.dto.ShopResponseDto;
import com.saym.eventory.shop.application.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopService shopService;

    // 가게 등록
    @PostMapping
    @Operation(summary = "가게 등록", description = "가게를 등록합니다.")
    public ResponseEntity<RspTemplate<Long>> createShop (@RequestBody ShopRequestDto shopRequestDto) {
        Long Id = shopService.createShop(shopRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RspTemplate.success(HttpStatus.CREATED, "가게 등록 성공", Id));
    }

    // 가게 수정
    @PutMapping("/{shopId}")
    @Operation(summary = "가게 수정", description = "가게 정보를 수정합니다.")
    public ResponseEntity<RspTemplate<Void>> updateShop (@PathVariable Long shopId, @RequestBody ShopRequestDto shopRequestDto) {
        shopService.updateShop(shopId, shopRequestDto);
        return ResponseEntity.ok(RspTemplate.success(HttpStatus.OK, "가게 수정 성공", null));
    }

    // 가게 삭제
    @DeleteMapping("/{shopId}")
    @Operation(summary = "가게 삭제", description = "가게를 삭제합니다.")
    public ResponseEntity<RspTemplate<Void>> deleteShop (@PathVariable Long shopId){
        shopService.deleteShop(shopId);
        return ResponseEntity.ok(RspTemplate.success(HttpStatus.OK, "가게 삭제 성공", null));
    }

    // 가게 상세 조회 (메뉴, 쿠폰, 리뷰 포함)
    @GetMapping("/detail/{shopId}")
    @Operation(method = "GET", summary = "가게 상세 조회", description = "특정 가게의 상세 정보와 메뉴, 쿠폰, 리뷰를 조회합니다.")
    public ResponseEntity<RspTemplate<ShopResponseDto>> getShopDetail(
            @PathVariable Long shopId
    ) {
        ShopResponseDto shopDetail = shopService.getShopDetail(shopId);

        RspTemplate<ShopResponseDto> response = RspTemplate.success(
                HttpStatus.OK,
                "가게 상세 조회 성공",
                shopDetail
        );
        return ResponseEntity.ok(response);
    }

}
