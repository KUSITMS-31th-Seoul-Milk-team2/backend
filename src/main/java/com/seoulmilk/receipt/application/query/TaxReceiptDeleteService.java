package com.seoulmilk.receipt.application.query;

import com.seoulmilk.receipt.domain.InValidReceiptRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaxReceiptDeleteService {
    private final InValidReceiptRepository inValidReceiptRepository;
    private final ValidReceiptRepository validReceiptRepository;

    @Transactional
    public void deleteValidReceipt(Long pk) {
        validReceiptRepository.deleteById(pk);
    }

    @Transactional
    public void deleteInvalidReceipt(Long pk) {
        inValidReceiptRepository.deleteById(pk);
    }

    @Transactional
    public void deleteInValidReceipts(List<Long> pkList) {
        inValidReceiptRepository.deleteByIds(pkList);
    }
}
