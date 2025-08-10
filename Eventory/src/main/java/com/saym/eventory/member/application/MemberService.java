package com.saym.eventory.member.application;

import com.saym.eventory.common.exception.CustomException;
import com.saym.eventory.common.exception.Error;
import com.saym.eventory.member.api.dto.request.ChangeUserTypeRequestDto;
import com.saym.eventory.member.api.dto.response.MemberResponseDto;
import com.saym.eventory.member.domain.Member;
import com.saym.eventory.member.domain.UserType;
import com.saym.eventory.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // RefreshToken으로 회원 찾기
    @Transactional(readOnly = true)
    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new CustomException(Error.REFRESH_TOKEN_EXPIRED_EXCEPTION, Error.REFRESH_TOKEN_EXPIRED_EXCEPTION.getMessage())
        );
    }

    @Transactional
    public MemberResponseDto changeUserType(Long memberId, ChangeUserTypeRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, Error.MEMBER_NOT_FOUND.getMessage()));

        UserType requestedType = dto.userType();

        if (requestedType == UserType.GENERAL) {
            // 사진 없이 바로 변경 (승인 처리까지 한번에)
            member.changeUserType(UserType.GENERAL, null);
        } else {
            // ORGANIZER, OWNER는 사업자등록증 이미지 필수
            if (dto.businessLicenseUrl() == null || dto.businessLicenseUrl().isEmpty()) {
                throw new CustomException(Error.BUSINESS_LICENSE_REQUIRED, Error.BUSINESS_LICENSE_REQUIRED.getMessage());
            }
            member.changeUserType(requestedType, dto.businessLicenseUrl());
        }

        return toResponse(member);
    }

    // 관리자 승인
    @Transactional
    public MemberResponseDto approveBusiness(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, Error.MEMBER_NOT_FOUND.getMessage()));
        member.approveBusiness();
        return toResponse(member);
    }

    // 관리자 거부
    @Transactional
    public MemberResponseDto rejectBusiness(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(Error.MEMBER_NOT_FOUND, Error.MEMBER_NOT_FOUND.getMessage()));
        member.rejectBusiness();
        return toResponse(member);
    }

    private MemberResponseDto toResponse(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole(),
                member.getUserType(),
                member.getApprovalStatus(),
                member.getPictureUrl(),
                member.getBusinessLicenseUrl()
        );
    }
}