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
        // 영수증 하나를 PK값을 이용하여 삭제합니다.
        validReceiptRepository.deleteById(pk);
    }

    @Transactional
    public void deleteInValidReceipts(List<Long> pkList) {
        // 불일치 영수증 여러개를 Pk 값의 리스트를 사용하여 제거합니다
        inValidReceiptRepository.deleteByIds(pkList);
    }
}
