package com.seoulmilk.receipt.application;

import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.PageValidReceiptResponse;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptSearchService {
    private final ValidReceiptRepository validReceiptRepository;
    private final InValidReceiptRepository invalidReceiptRepository;

    public PageValidReceiptResponse<ValidReceipt> findAllValidReceiptPage(
            ValidResponseSearchRequest keywords,
            Pageable pageable
    ) {
        Page<ValidReceipt> validReceipts = validReceiptRepository.findAllBySpecification(keywords, pageable);

        List<ValidReceipt> result = validReceipts.getContent();
        log.info("[findAllValidReceiptPage] result - {}", result);
        return PageValidReceiptResponse.create(result, validReceipts);
    }
}
