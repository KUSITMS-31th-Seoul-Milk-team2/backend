package com.seoulmilk.receipt.presentation;

import com.seoulmilk.core.presentation.RestResponse;
import com.seoulmilk.receipt.application.ASyncTaxReceiptValidationService;
import com.seoulmilk.receipt.application.TaxReceiptValidationService;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationRequest;
import com.seoulmilk.receipt.presentation.dto.request.TaxReceiptValidationWithAuthRequest;
import com.seoulmilk.receipt.presentation.dto.response.AdditionalAuthResponse;
import com.seoulmilk.receipt.presentation.dto.response.TaxReceiptValidationResponse;
import com.seoulmilk.receipt.presentation.swagger.TaxReceiptValidateSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/receipt")
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptValidationController implements TaxReceiptValidateSwagger {
    private final TaxReceiptValidationService taxReceiptValidationService;
    private final ASyncTaxReceiptValidationService asyncTaxReceiptValidationService;

    @Override
    @PostMapping("/validation")
    public ResponseEntity<RestResponse<AdditionalAuthResponse>> validTaxReceipt(
            @RequestBody TaxReceiptValidationRequest request
    ){
        log.info("[validTaxReceipt] 컨트롤러 작동");
        AdditionalAuthResponse additionalAuthResponse = taxReceiptValidationService.validateTaxReceipt(request);
        return ResponseEntity.ok(new RestResponse<>(additionalAuthResponse));
    }

    @Override
    @PostMapping("/addition")
    public ResponseEntity<RestResponse<TaxReceiptValidationResponse>> additionAuthController(
            @RequestBody TaxReceiptValidationWithAuthRequest request
    ){
        log.info("[additionAuthController] 컨트롤러 작동");
        TaxReceiptValidationResponse taxReceiptValidationResponse =
                taxReceiptValidationService.validationWithAdditionalAuth(request);

        return ResponseEntity.ok(new RestResponse<>(taxReceiptValidationResponse));
    }

    @Override
    @PostMapping("multiple-validation")
    public Mono<ResponseEntity<RestResponse<List<AdditionalAuthResponse>>>> validateTaxReceipts(
            @RequestBody List<TaxReceiptValidationRequest> requestList
    ) {
        return asyncTaxReceiptValidationService.multipleRecieptValidation(requestList)
                .map(responseList -> ResponseEntity.ok(new RestResponse<>(responseList)))
                .doOnSuccess(res -> log.info("추가 요청 데이터 전송 성공: {}", res))
                .doOnError(err -> log.error("추가 요청 데이터 전송 실패", err));
    }

    @Override
    @PostMapping("multiple-addition")
    public Mono<ResponseEntity<RestResponse<List<TaxReceiptValidationResponse>>>> multipleAdditionAuthController(
            @RequestBody List<TaxReceiptValidationWithAuthRequest> requestList
    ) {
        return asyncTaxReceiptValidationService.multipleValidationWithAuth(requestList)
                .map(responseList -> ResponseEntity.ok(new RestResponse<>(responseList)))
                .doOnSuccess(res -> log.info("세금계산서 인증 성공: {}", res))
                .doOnError(err -> log.info("세금 계산서 인증 실패", err));
    }
}
