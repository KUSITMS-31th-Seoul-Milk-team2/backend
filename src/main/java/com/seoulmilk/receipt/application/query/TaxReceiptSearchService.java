package com.seoulmilk.receipt.application.query;

import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptSearchService {
    private final ValidReceiptRepository validReceiptRepository;
    private final InValidReceiptRepository invalidReceiptRepository;

    public List<ValidReceipt> findAllValidReceiptPage(
            ValidResponseSearchRequest keywords
    ) {
        return validReceiptRepository.findAllBySpecification(keywords);
    }
}
