package com.saym.eventory.store.api.dto.request;

import com.saym.eventory.store.api.dto.request.MenuRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.time.LocalTime;

public record StoreRequestDto(
        @Schema(description = "가게 이름", example = "Eventory")
        String name,

        @Schema(description = "전화번호 ('-' 포함, ex: 02-123-4567)", example = "02-123-4567")
        String phoneNumber,

        @Schema(description = "오픈 시간 (HH:mm, 24시간 기준)", example = "09:00")
        LocalTime openTime,

        @Schema(description = "마감 시간 (HH:mm, 24시간 기준)", example = "21:00")
        LocalTime closeTime,

        @Schema(description = "주소", example = "서울시 강남구 ...")
        String address,

        @Schema(description = "상세 주소", example = "2층")
        String addressDetail,

        @Schema(description = "주차 안내", example = "건물 앞 주차 가능 or 없음")
        String parkingNote,

        @Schema(description = "메뉴 목록")
        List<MenuRequestDto> menus,

        @Schema(description = "대표 이미지 파일")
        MultipartFile pictureFile

) {}