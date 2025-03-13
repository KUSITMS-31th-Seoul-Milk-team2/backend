package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.core.infrastructure.security.CustomUserDetails;
import com.seoulmilk.emp.domain.entity.Emp;
import com.seoulmilk.emp.domain.repository.EmpRepository;
import com.seoulmilk.receipt.domain.ValidReceiptRepository;
import com.seoulmilk.receipt.domain.entity.ValidReceipt;
import com.seoulmilk.receipt.dto.request.UpdateValidReceiptRequest;
import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.exception.ReceiptErrorCode;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import com.seoulmilk.receipt.infrastructure.persistence.mapper.ValidReceiptMapper;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.repository.ValidReceiptJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    public Optional<ValidReceipt> findById(Long id) {
        return validReceiptJpaRepository.findById(id).map(validReceiptMapper::toDomainEntity);
    }

    @Override
    public Optional<ValidReceipt> findByIssueId(String issueId) {
        return validReceiptJpaRepository.findByIssueId(issueId).map(validReceiptMapper::toDomainEntity);
    }

    @Override
    public void deleteAll() {
        validReceiptJpaRepository.deleteAll();
    }

    @Override
    public void deleteById(Long pk) {
        validReceiptJpaRepository.deleteById(pk);
    }

    @Override
    public int update(UpdateValidReceiptRequest updateValidReceiptRequest) {
        return validReceiptJpaRepository.update(updateValidReceiptRequest);
    }

    @Override
    public List<ValidReceipt> findAllBySpecificationWithAdmin(
            ValidResponseSearchRequest request
    ) {
        List<String> empNames = request.employeeName();
        List<String> empIds = new ArrayList<>();

        for(String empName : empNames) {
            List<Emp> employees = empRepository.findAllByName(empName);
            employees.forEach(emp -> empIds.add(emp.getEmployeeId()));
        }

        ValidResponseSearchRequest validResponseSearchRequest = new ValidResponseSearchRequest(
                empIds,
                request.suNames(),
                request.ipNames(),
                request.erdatStart(),
                request.erdatEnd()
        );

        return validReceiptJpaRepository.findAll(
                ValidReceiptSpecification.search(
                        validResponseSearchRequest),
                Sort.by(Sort.Direction.DESC, "id")
        )
                .stream()
                .map(ValidReceipt::toDomainEntity).toList();
    }

    @Override
    public List<ValidReceipt> findAllBySpecification(
            CustomUserDetails customUserDetails,
            ValidResponseSearchRequest request
    ) {
        List<String> empIds = null;
        List<Emp> employees = empRepository.findAllByName(customUserDetails.getUsername());

        for(Emp emp : employees){
            if(customUserDetails.getId() == emp.getId()){
                empIds = new ArrayList<>();
                empIds.add(emp.getEmployeeId());
                break;
            }
        }

        ValidResponseSearchRequest validResponseSearchRequest = new ValidResponseSearchRequest(
                empIds,
                request.suNames(),
                request.ipNames(),
                request.erdatStart(),
                request.erdatEnd()
        );

        return validReceiptJpaRepository.findAll(
                ValidReceiptSpecification.search(
                        validResponseSearchRequest),
                        Sort.by(Sort.Direction.DESC, "id")
                )
                .stream()
                .map(ValidReceipt::toDomainEntity).toList();
    }

    @Override
    public List<ValidReceipt> findAll() {
        return validReceiptJpaRepository.findAllByIsShowTrue().stream().map(ValidReceipt::toDomainEntity).toList();
    }

    @Override
    public int bulkUpdateExpiredReceipts(LocalDateTime cutoff) {
        return validReceiptJpaRepository.bulkUpdateExpiredReceipts(cutoff);
    }
}
