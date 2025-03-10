package com.seoulmilk.notice.infrastructure.persistence.jpa.specification;

import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public class NoticeSpecifications {

    private static String createContainingPattern(String keyword) {
        return "%" + keyword + "%";
    }

    public static Specification<NoticeJpaEntity> likeAll(String keyword) {
        String containingPattern = createContainingPattern(keyword);
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), containingPattern),
                criteriaBuilder.like(root.get("content"), containingPattern),
                criteriaBuilder.like(root.get("authorName"), containingPattern)
        );
    }

    public static Specification<NoticeJpaEntity> likeTitleAndContent(String keyword) {
        String containingPattern = createContainingPattern(keyword);
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), containingPattern),
                criteriaBuilder.like(root.get("content"), containingPattern)
        );
    }

    public static Specification<NoticeJpaEntity> likeAuthorName(String keyword) {
        String containingPattern = createContainingPattern(keyword);
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get("authorName"), containingPattern
        );
    }
}
