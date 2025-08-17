package com.saym.eventory.member.api;

import com.saym.eventory.common.template.RspTemplate;
import com.saym.eventory.member.api.dto.request.ChangeUserTypeRequestDto;
import com.saym.eventory.member.api.dto.response.MemberResponseDto;
import com.saym.eventory.member.application.MemberService;
import com.saym.eventory.member.domain.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API", description = "사용자 관련 API 입니다. (사용자 인증, 사용자 정보)")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "사용자 유형 변경 요청 (사업자등록증 이미지 업로드 포함), GENERAL은 null 허용하여 바로 승인됩니다.",
            description = """
        사용자의 `userType`과 `approvalStatus`에 따라 앱 진입 시 화면이 다르게 표시됩니다.
        
        * `userType`이 `null`인 경우: 사용자 유형 선택 화면으로 이동합니다.
        
        * `userType`이 `ORGANIZER` 또는 `OWNER`인 경우:
            * `approvalStatus`가 `PENDING`이면, 인증 대기 화면으로 이동합니다.
            * `approvalStatus`가 `APPROVED`이면, 메인 화면으로 이동합니다.
            * `approvalStatus`가 `REJECTED`이면, 인증 거부 알림 후 서류 재제출 화면으로 이동합니다.
            
        * `userType`이 `GENERAL`인 경우: 메인 화면으로 바로 이동합니다.
        """
    )
    @PatchMapping(value = "/user-type", consumes = {"multipart/form-data"})
    public RspTemplate<MemberResponseDto> changeUserType(
            @RequestPart("userType") String userTypeStr,
            @RequestPart(value = "businessLicenseFile", required = false) MultipartFile file,
            Principal principal
    ) {
        Long memberId = Long.parseLong(principal.getName()); // 로그인한 유저의 memberId
        ChangeUserTypeRequestDto dto = new ChangeUserTypeRequestDto(
                UserType.valueOf(userTypeStr),
                file
        );
        return RspTemplate.ok(memberService.changeUserType(memberId, dto));
    }

    @Operation(summary = "사업자 승인 | 실제 사용하지 않는 api 입니다. (편리를 위해 작성)")
    @PostMapping("/{memberId}/approve")
    public RspTemplate<MemberResponseDto> approveBusiness(@PathVariable Long memberId) {
        return RspTemplate.ok(memberService.approveBusiness(memberId));
    }

    @Operation(summary = "사업자 승인 거부 | 실제 사용하지 않는 api 입니다. (편리를 위해 작성)")
    @PostMapping("/{memberId}/reject")
    public RspTemplate<MemberResponseDto> rejectBusiness(@PathVariable Long memberId) {
        return RspTemplate.ok(memberService.rejectBusiness(memberId));
    }
}
