package com.saym.eventory.store.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.store.api.dto.request.MenuRequestDto;
import com.saym.eventory.store.api.dto.request.StoreRequestDto;
import com.saym.eventory.store.api.dto.response.StoreResponseDto;
import com.saym.eventory.store.application.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
@Tag(name = "Store API", description = "가게 관련 API 입니다. (가맹점주 모드)")
public class StoreController {

    private final StoreService storeService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "가게 등록 (가맹점주만 가능)",
            description = "**Responses 의 data 속 id 가 storeId 입니다.** <br>"
                    + "메뉴와 쿠폰을 제외한 모든 필드는 필수 입력입니다. <br>" +
                    "전화번호는 '-' 포함 ex) 02-123-4567 <br>" +
                    "오픈/마감 시간은 HH:mm 형식 (24시간 기준)으로 작성해주세요. <br>" +
                    "브레이크 타임은 HH:mm 형식 (24시간 기준)으로 시작/종료 시간 입력 가능합니다. (선택 사항) <br>" +
                    "정기 휴일은 자유롭게 텍스트로 입력 가능합니다. (선택 사항) <br>" +
                    "대표 이미지는 필수 입니다. <br> 메뉴 및 쿠폰 정보는 JSON 배열로 전달 가능합니다. <br> ex) [ {\"menuName\": \"에그마요 샌드위치\", \"price\": 6500, \"isSignature\": true}, {\"menuName\": \"햄치즈 샌드위치\", \"price\": 7000, \"isSignature\": false} ]"
    )
    @PostMapping(consumes = {"multipart/form-data"})
    public RspTemplate<StoreResponseDto> createStore(
            Principal principal,
            @RequestPart("name") String name,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("openTime") String openTime,
            @RequestPart("closeTime") String closeTime,
            @RequestPart(value = "breakTimeStart", required = false) String breakTimeStart,
            @RequestPart(value = "breakTimeEnd", required = false) String breakTimeEnd,
            @RequestPart(value = "regularDayOffNote", required = false) String regularDayOffNote,
            @RequestPart("address") String address,
            @RequestPart("addressDetail") String addressDetail,
            @RequestPart("parkingNote") String parkingNote,
            @RequestPart(value = "pictureFile", required = false) MultipartFile pictureFile,
            @RequestPart(value = "menus", required = false) String menusJson
    ) {
        List<MenuRequestDto> menus = parseMenus(menusJson);

        StoreRequestDto requestDto = new StoreRequestDto(
                name,
                phoneNumber,
                LocalTime.parse(openTime),
                LocalTime.parse(closeTime),
                breakTimeStart != null ? LocalTime.parse(breakTimeStart) : null,
                breakTimeEnd != null ? LocalTime.parse(breakTimeEnd) : null,
                regularDayOffNote,
                address,
                addressDetail,
                parkingNote,
                menus,
                pictureFile
        );

        return RspTemplate.ok(storeService.createStore(principal, requestDto));
    }

    @Operation(
            summary = "가게 정보 수정 (가맹점주만 가능)",
            description = "**메뉴와 쿠폰을 제외한 모든 필드는 필수 입력입니다.** <br>" +
                    "전화번호는 '-' 포함 (ex: 02-123-4567), <br>" +
                    "오픈/마감 시간은 HH:mm 형식 (24시간 기준)으로 작성해주세요. <br>" +
                    "브레이크 타임은 HH:mm 형식 (24시간 기준)으로 시작/종료 시간 입력 가능합니다. (선택 사항, 필수 입력 X) <br>" +
                    "정기 휴일은 자유롭게 텍스트로 입력 가능합니다. (선택 사항, 필수 입력 X) <br>" +
                    "대표 이미지는 필수 입니다. <br> 메뉴 및 쿠폰 정보는 JSON 배열로 전달 가능합니다. <br> ex) [ {\"menuName\": \"에그마요 샌드위치\", \"price\": 6500, \"isSignature\": true}, {\"menuName\": \"햄치즈 샌드위치\", \"price\": 7000, \"isSignature\": false} ]"
    )
    @PatchMapping(value = "/{storeId}", consumes = {"multipart/form-data"})
    public RspTemplate<StoreResponseDto> updateStore(
            Principal principal,
            @PathVariable Long storeId,
            @RequestPart("name") String name,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("openTime") String openTime,
            @RequestPart("closeTime") String closeTime,
            @RequestPart(value = "breakTimeStart", required = false) String breakTimeStart,
            @RequestPart(value = "breakTimeEnd", required = false) String breakTimeEnd,
            @RequestPart(value = "regularDayOffNote", required = false) String regularDayOffNote,
            @RequestPart("address") String address,
            @RequestPart("addressDetail") String addressDetail,
            @RequestPart("parkingNote") String parkingNote,
            @RequestPart(value = "pictureFile", required = false) MultipartFile pictureFile,
            @RequestPart(value = "menus", required = false) String menusJson
    ) {
        List<MenuRequestDto> menus = parseMenus(menusJson);

        StoreRequestDto requestDto = new StoreRequestDto(
                name,
                phoneNumber,
                LocalTime.parse(openTime),
                LocalTime.parse(closeTime),
                breakTimeStart != null ? LocalTime.parse(breakTimeStart) : null,
                breakTimeEnd != null ? LocalTime.parse(breakTimeEnd) : null,
                regularDayOffNote,
                address,
                addressDetail,
                parkingNote,
                menus,
                pictureFile
                );

        return RspTemplate.ok(storeService.updateStore(principal, storeId, requestDto));
    }

    @Operation(summary = "가게 삭제 (가게 소유자만 가능)")
    @DeleteMapping("/{storeId}")
    public RspTemplate<Void> deleteStore(Principal principal, @PathVariable Long storeId) {
        storeService.deleteStore(principal, storeId);
        return RspTemplate.ok(null);
    }

    @Operation(summary = "가게 상세 정보 조회 (모든 사용자 접근 가능)")
    @GetMapping("/{storeId}")
    public RspTemplate<StoreResponseDto> getStoreDetails(@PathVariable Long storeId) {
        return RspTemplate.ok(storeService.getStoreDetails(storeId));
    }

    @Operation(summary = "내가 등록한 가게 정보 조회 (가맹점주만 가능)")
    @GetMapping("/my")
    public RspTemplate<StoreResponseDto> getMyStore(Principal principal) {
        return RspTemplate.ok(storeService.getMyStore(principal));
    }

    // 메뉴 JSON 파싱을 별도 메서드로 분리
    private List<MenuRequestDto> parseMenus(String menusJson) {
        if (menusJson == null || menusJson.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(menusJson, new TypeReference<List<MenuRequestDto>>() {});
        } catch (IOException e) {
            throw new RuntimeException("메뉴 정보 파싱에 실패했습니다.", e);
        }
    }
}
