package com.seoulmilk.receipt.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TaxReceiptValidationWithAuthRequest(
        @NotNull(message = "기관명은 필수 입력 항목입니다.")
        @Schema(description = "기관코드", example = "0004(고정값)")
        String organization,

        @NotNull(message = "로그인 구분은 필수 입력 항목입니다.")
        @Schema(description = "로그인 구분", example = "0 -> 인증서 로그인, 5 -> 간편인증")
        String loginType,

        @NotNull(message = "사용자를 구분하기 위한 유일값을 세팅해주세요")
        @Schema(description = "사용자 계정을 식별할 수 있는 유일 값 세팅(아이디 또는 주민번호 해시값)", example = "u283-d93j-doe2-3we4")
        String id,

        @NotNull(message = "간편인증시 로그인 구분은 필수 인증 값입니다.")
        @Schema(description = "간편인증 로그인 구분", example = "1:카카오톡, 2:페이코, 3:삼성패스, 4:KB모바일, 5:통신사(PASS), " +
                "6:네이버, 7:신한인증서, 8: toss, 9: 뱅크샐러드")
        String loginTypeLevel,

        @NotNull(message = "사용자 이름은 필수 입력 항목입니다.")
        @Schema(description = "사용자 이름", example = "홍길동")
        String userName,

        @NotNull(message = "전화번호는 필수 입력 항목입니다.")
        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNo,

        @NotNull(message = "사용자 주민번호는 필수 입력 항목입니다.")
        @Schema(description = "사용자 주민번호 앞자리", example = "YYYYMMDD")
        String identity,

        @NotNull(message = "공급자 등록 번호는 필수 입력 항목입니다.")
        @Schema(description = "공급자 등록 번호", example = "10자리 숫자 (1234567890)")
        String supplierRegNumber,

        @NotNull(message = "공급 받는자 등록번호는 필수 입력 항목입니다.")
        @Schema(description = "공급받는자 등록번호", example = "10자리 숫자 (1234567890)")
        String contractorRegNumber,

        @NotNull(message = "승인번호는 필수 입력 항목입니다.")
        @Schema(description = "승인번호", example = "숫자만 입력하셔야 합니다")
        String approvalNo,

        @NotNull(message = "작성일자는 필수 입력 항목입니다.")
        @Schema(description = "작성일자", example = "YYYYMMDD")
        String reportingDate,

        @NotNull(message = "공급가액은 필수 입력 항목입니다.")
        @Schema(description = "공급가액", example = "4560000")
        String supplyValue,

        @NotNull(message = "간편인증시 통신사는 필수 입력 항목입니다.")
        @Schema(description = "통신사", example = "“0\":SKT(SKT알뜰폰), “1”:KT(KT알뜰폰), “2\":LG U+(LG U+알뜰폰)")
        String telecom,

        @NotNull(message = "간편인증 여부는 필수 입력 항목입니다")
        @Schema(description = "간편인증 여부", example = "1")
        String simpleAuth,

        @NotNull(message = "추가 요청 설명값을 입력해주세요(true 고정)")
        @Schema(description = "추가 요청임을 알려주는 설정값", example = "true")
        Boolean is2Way,

        @Schema(description = "1차 요청 응답 값", example = "{}")
        TwoWayInfo twoWayInfo
) {
    public record TwoWayInfo(
            @NotNull(message = "잡 인덱스는 필수 입력 값입니다.(응답값과 동일)")
            @Schema(description = "잡 인덱스", example = "0")
            Integer jobIndex,

            @NotNull(message = "스레드 인덱스는 필수 입력 값입니다.(응답값과 동일)")
            @Schema(description = "스레드 인덱스", example = "0")
            Integer threadIndex,

            @NotNull(message = "트랜잭션 아이디를 필수로 입력해야 합니다.(응답값과 동일)")
            @Schema(description = "트랜잭션 아이디", example = "db55392ae72a44efaa394")
            String jti,

            @NotNull(message = "추가 인증 시간을 필수로 입력해야 합니다.(응답값과 동일)")
            @Schema(description = "추가 인증 시간", example = "15650663")
            Long twoWayTimestamp
    ) {
    }
}
