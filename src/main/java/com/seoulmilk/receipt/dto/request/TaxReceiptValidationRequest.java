package com.seoulmilk.receipt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TaxReceiptValidationRequest(
        @NotNull(message = "기관명은 필수 입력 항목입니다.")
        @Schema(description = "기관코드", example = "0004")
        String organization,

        @NotNull(message = "로그인 구분은 필수 입력 항목입니다.")
        @Schema(description = "로그인 구분", example = "5")
        String loginType,

        @NotNull(message = "사용자를 구분하기 위한 유일값을 세팅해주세요")
        @Schema(description = "사용자 계정을 식별할 수 있는 유일 값 세팅(아이디 또는 주민번호 해시값)", example = "u283-d93j-doe2-3we4")
        String id,

        @NotNull(message = "간편인증시 로그인 구분은 필수 인증 값입니다.")
        @Schema(description = "간편인증 로그인 구분", example = "1")
        String loginTypeLevel,

        @NotNull(message = "사용자 이름은 필수 입력 항목입니다.")
        @Schema(description = "사용자 이름", example = "윤창현")
        String userName,

        @NotNull(message = "전화번호는 필수 입력 항목입니다.")
        @Schema(description = "전화번호", example = "개인 전화번호(숫자만)")
        String phoneNo,

        @NotNull(message = "사용자 주민번호는 필수 입력 항목입니다.")
        @Schema(description = "사용자 주민번호 앞자리", example = "개인정보(YYYYMMDD)")
        String identity,

        @NotNull(message = "공급자 등록 번호는 필수 입력 항목입니다.")
        @Schema(description = "공급자 등록 번호", example = "3062870320")
        String supplierRegNumber,

        @NotNull(message = "공급 받는자 등록번호는 필수 입력 항목입니다.")
        @Schema(description = "공급받는자 등록번호", example = "3088509085")
        String contractorRegNumber,

        @NotNull(message = "승인번호는 필수 입력 항목입니다.")
        @Schema(description = "승인번호", example = "202406304100000578475123")
        String approvalNo,

        @NotNull(message = "작성일자는 필수 입력 항목입니다.")
        @Schema(description = "작성일자", example = "20240630")
        String reportingDate,

        @NotNull(message = "공급가액은 필수 입력 항목입니다.")
        @Schema(description = "공급가액", example = "23930493")
        String supplyValue,

        @NotNull(message = "간편인증시 통신사는 필수 입력 항목입니다.")
        @Schema(description = "통신사", example = "0")
        String telecom
) {}

