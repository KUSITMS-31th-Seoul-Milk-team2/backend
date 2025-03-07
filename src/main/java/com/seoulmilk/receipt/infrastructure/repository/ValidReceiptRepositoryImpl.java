package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.ValidReceiptMapper;
import com.seoulmilk.receipt.infrastructure.persistence.repository.ValidReceiptJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ValidReceiptRepositoryImpl implements ValidReceiptRepository {
    private final ValidReceiptMapper validReceiptMapper;
    private final ValidReceiptJpaRepository validReceiptJpaRepository;
    private final EmpRepository empRepository;

    @Override
    public ValidReceipt save(ValidReceipt validReceipt) {
        ValidReceiptJpaEntity validReceiptJpaEntity = validReceiptMapper.toJpaEntity(validReceipt);
        if(validReceiptJpaEntity == null) {
            throw ReceiptErrorCode.FAILED_TO_SAVE_RECEIPT.toException();
        }
        validReceiptJpaRepository.save(validReceiptJpaEntity);
        return validReceiptMapper.toDomainEntity(validReceiptJpaEntity);
    }

    @Override
    public Optional<ValidReceipt> findById(String issueId) {
        return validReceiptJpaRepository.findByIssueId(issueId).map(validReceiptMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        validReceiptJpaRepository.deleteAll();
    }

    @Override
    public List<ValidReceipt> findAllBySpecification(
            ValidResponseSearchRequest validResponseSearchRequest
    ) {
        List<String> empNames = validResponseSearchRequest.employeeName();
        List<String> empIds = new ArrayList<>();

        for(String empName : empNames) {
            List<Emp> employees = empRepository.findAllByName(empName); // 여러 개 조회
            employees.forEach(emp -> empIds.add(emp.getEmployeeId()));
        }

        ValidResponseSearchRequest v = new ValidResponseSearchRequest(
                empIds,
                validResponseSearchRequest.suNames(),
                validResponseSearchRequest.ipNames(),
                validResponseSearchRequest.erdatStart(),
                validResponseSearchRequest.erdatEnd()
        );

       return validReceiptJpaRepository.findAll(
            ValidReceiptSpecification.search(
                    v
            )
       ).stream().map(ValidReceipt::toDomainEntity).toList();
    }
}
