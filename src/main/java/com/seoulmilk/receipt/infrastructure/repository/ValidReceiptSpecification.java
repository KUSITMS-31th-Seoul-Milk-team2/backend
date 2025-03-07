package com.seoulmilk.receipt.infrastructure.repository;

import com.seoulmilk.receipt.dto.request.ValidResponseSearchRequest;
import com.seoulmilk.receipt.infrastructure.persistence.jpa.entity.ValidReceiptJpaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ValidReceiptSpecification {
    public static Specification<ValidReceiptJpaEntity> search(
            ValidResponseSearchRequest validResponseSearchRequest
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 직원 ID 검색
            if (validResponseSearchRequest.employeeId() != null && !validResponseSearchRequest.employeeId().isEmpty()) {
                predicates.add(root.get("employeeId").in(validResponseSearchRequest.employeeId()));
            }

            // 공급업체 이름 여러 개 (IN 절)
            if (validResponseSearchRequest.suNames() != null && !validResponseSearchRequest.suNames().isEmpty()) {
                predicates.add(root.get("suName").in(validResponseSearchRequest.suNames()));
            }

            // 수취인 이름 여러 개 (IN 절)
            if (validResponseSearchRequest.ipNames() != null && !validResponseSearchRequest.ipNames().isEmpty()) {
                predicates.add(root.get("ipName").in(validResponseSearchRequest.ipNames()));
            }

            // 기간 검색 (날짜 범위)
            if (validResponseSearchRequest.erdatStart() != null && validResponseSearchRequest.erdatEnd() != null) {
                predicates.add(
                        criteriaBuilder.between(
                                root.get("erdat"),
                                validResponseSearchRequest.erdatStart(),
                                validResponseSearchRequest.erdatEnd()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
