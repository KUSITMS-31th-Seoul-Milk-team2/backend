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

            // 직원 ID 검색 - 유효성 검사 추가
            if (validResponseSearchRequest.employeeName() != null && !validResponseSearchRequest.employeeName().isEmpty()) {
                List<String> sanitizedEmployeeName = sanitizeListInput(validResponseSearchRequest.employeeName());
                predicates.add(root.get("employeeId").in(sanitizedEmployeeName));
            }

            // 공급업체 이름 여러 개 (IN 절)
            if (validResponseSearchRequest.suNames() != null && !validResponseSearchRequest.suNames().isEmpty()) {
                List<String> sanitizedSuNames = sanitizeListInput(validResponseSearchRequest.suNames());
                predicates.add(root.get("suName").in(sanitizedSuNames));
            }

            // 수취인 이름 여러 개 (IN 절)
            if (validResponseSearchRequest.ipNames() != null && !validResponseSearchRequest.ipNames().isEmpty()) {
                List<String> sanitizedIpNames = sanitizeListInput(validResponseSearchRequest.ipNames());
                predicates.add(root.get("ipName").in(sanitizedIpNames));
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

    private static String sanitizeInput(String input) {
        return input.replaceAll("[^a-zA-Z0-9가-힣]", "");
    }

    private static List<String> sanitizeListInput(List<String> inputList) {
        List<String> sanitizedList = new ArrayList<>();
        for (String input : inputList) {
            sanitizedList.add(sanitizeInput(input));
        }
        return sanitizedList;
    }
}
