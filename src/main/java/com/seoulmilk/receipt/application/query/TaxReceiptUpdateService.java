package com.seoulmilk.receipt.application.query;

import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptUpdateService {
    private final ValidReceiptRepository validReceiptRepository;

    @Transactional
    public void updateValidReceipt(UpdateValidReceiptRequest updateValidReceiptRequest) {
        ValidReceipt validReceipt = validReceiptRepository.findById(updateValidReceiptRequest.id())
                .orElseThrow(() -> ReceiptErrorCode.NOT_EXIST_RECEIPT.toException());

        validReceiptRepository.update(updateValidReceiptRequest);
    }
}
