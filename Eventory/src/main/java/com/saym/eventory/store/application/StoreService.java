package com.saym.eventory.store.application;

import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.global.s3.service.S3Service;
import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.UserType;
import com.saym.eventory.member.domain.repository.MemberRepository;
import com.saym.eventory.store.domain.Menu;
import com.saym.eventory.store.api.dto.request.StoreRequestDto;
import com.saym.eventory.store.api.dto.response.StoreResponseDto;
import com.saym.eventory.store.domain.Store;
import com.saym.eventory.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    // 가게 생성
    public StoreResponseDto createStore(Principal principal, StoreRequestDto requestDto) {
        Long ownerId = Long.parseLong(principal.getName());
        Member owner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, Error.MEMBER_NOT_FOUND.getMessage()));

        if (owner.getUserType() != UserType.OWNER) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "가맹점주만 가게를 생성할 수 있습니다.");
        }
        if (storeRepository.existsByOwnerId(ownerId)) {
            throw new CustomException(Error.STORE_ALREADY_EXISTS, "이미 가게를 등록했습니다.");
        }
        if (requestDto.pictureFile() == null || requestDto.pictureFile().isEmpty()) {
            throw new CustomException(Error.PICTURE_REQUIRED, "가게 대표 이미지는 필수입니다.");
        }

        String pictureUrl;
        try {
            pictureUrl = s3Service.uploadFile(requestDto.pictureFile(), "store-pictures");
        } catch (IOException e) {
            log.error("가게 대표 이미지 업로드 실패: {}", e.getMessage(), e);
            throw new CustomException(Error.FILE_UPLOAD_FAILED, "가게 대표 이미지 업로드 실패했습니다.");
        }

        Store store = Store.builder()
                .owner(owner)
                .name(requestDto.name())
                .phoneNumber(requestDto.phoneNumber())
                .openTime(requestDto.openTime())
                .closeTime(requestDto.closeTime())
                .address(requestDto.address())
                .addressDetail(requestDto.addressDetail())
                .parkingNote(requestDto.parkingNote())
                .pictureUrl(pictureUrl)
                .build();

        // 메뉴 저장
        if (requestDto.menus() != null && !requestDto.menus().isEmpty()) {
            requestDto.menus().forEach(menuDto -> {
                Menu menu = Menu.builder()
                        .store(store)
                        .menuName(menuDto.menuName())
                        .price(menuDto.price())
                        .isSignature(menuDto.isSignature())
                        .build();
                store.getMenus().add(menu);
            });
        }

        Store savedStore = storeRepository.save(store);
        return StoreResponseDto.from(savedStore);
    }

    // 가게 정보 수정
    public StoreResponseDto updateStore(Principal principal, Long storeId, StoreRequestDto requestDto) {
        Long ownerId = Long.parseLong(principal.getName());
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND, Error.STORE_NOT_FOUND.getMessage()));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "가게 정보는 소유자만 수정할 수 있습니다.");
        }

        String pictureUrl = store.getPictureUrl();
        MultipartFile newPictureFile = requestDto.pictureFile();
        if (newPictureFile != null && !newPictureFile.isEmpty()) {
            try {
                if (pictureUrl != null && !pictureUrl.isEmpty()) {
                    s3Service.deleteFile(pictureUrl);
                }
                pictureUrl = s3Service.uploadFile(newPictureFile, "store-pictures");
            } catch (Exception e) {
                log.error("가게 대표 이미지 업데이트 실패: {}", e.getMessage(), e);
                throw new CustomException(Error.FILE_UPLOAD_FAILED, "가게 대표 이미지 업데이트 실패했습니다.");
            }
        }

        store.updateStore(
                requestDto.name(),
                requestDto.phoneNumber(),
                requestDto.openTime(),
                requestDto.closeTime(),
                requestDto.address(),
                requestDto.addressDetail(),
                pictureUrl,
                store.getDescription(),
                store.getOperatingHoursNote(),
                requestDto.parkingNote()
        );

        // 기존 메뉴 삭제 후 새 메뉴 저장
        store.getMenus().clear();
        if (requestDto.menus() != null && !requestDto.menus().isEmpty()) {
            requestDto.menus().forEach(menuDto -> store.getMenus().add(
                    Menu.builder()
                            .store(store)
                            .menuName(menuDto.menuName())
                            .price(menuDto.price())
                            .isSignature(menuDto.isSignature())
                            .build()
            ));
        }

        return StoreResponseDto.from(store);
    }

    // 가게 삭제
    public void deleteStore(Principal principal, Long storeId) {
        Long ownerId = Long.parseLong(principal.getName());
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND, Error.STORE_NOT_FOUND.getMessage()));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new CustomException(Error.UNAUTHORIZED_ACCESS, "가게 정보는 소유자만 삭제할 수 있습니다.");
        }

        if (store.getPictureUrl() != null && !store.getPictureUrl().isEmpty()) {
            try {
                s3Service.deleteFile(store.getPictureUrl());
            } catch (Exception e) {
                log.error("S3 파일 삭제 실패: {}", e.getMessage(), e);
            }
        }

        storeRepository.delete(store);
    }

    // 가게 상세 정보 조회
    @Transactional(readOnly = true)
    public StoreResponseDto getStoreDetails(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND, Error.STORE_NOT_FOUND.getMessage()));
        return StoreResponseDto.from(store);
    }

    // 본인이 등록한 가게 조회
    @Transactional(readOnly = true)
    public StoreResponseDto getMyStore(Principal principal) {
        Long ownerId = Long.parseLong(principal.getName());
        Store store = storeRepository.findByOwnerId(ownerId) // 👈 소유자 ID로 가게 조회
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND, "등록된 가게가 없습니다."));
        return StoreResponseDto.from(store);
    }
}
