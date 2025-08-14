package com.saym.eventory.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserType userType; // GENERAL, ORGANIZER, OWNER

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus; // PENDING, APPROVED, REJECTED

    private String businessLicenseUrl; // 사업자등록증 이미지 경로

    private String refreshToken;

    public void changeUserType(UserType userType, String businessLicenseUrl) {
        this.userType = userType;
        this.businessLicenseUrl = businessLicenseUrl;

        if (userType == UserType.GENERAL) {
            this.approvalStatus = ApprovalStatus.APPROVED; // GENERAL은 바로 승인
        } else {
            this.approvalStatus = ApprovalStatus.PENDING;  // 나머지는 승인 대기
        }
    }


    @Builder
    public Member(String name, String email, String password, Long kakaoId, Role role, String pictureUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
        this.role = role;
        this.pictureUrl = pictureUrl;
    }

    public void approveBusiness() {
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    public void rejectBusiness() {
        this.approvalStatus = ApprovalStatus.REJECTED;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateName(String name) {
        this.name = name;
    }

}
