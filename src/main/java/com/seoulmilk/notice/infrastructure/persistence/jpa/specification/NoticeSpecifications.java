package com.seoulmilk.notice.infrastructure.persistence.jpa.specification;

import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public class NoticeSpecifications {
    public static Specification<NoticeJpaEntity> likeAll(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("content"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("authorName"), "%" + keyword + "%")
        );
    }

    public static Specification<NoticeJpaEntity> likeTitleAndContent(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("content"), "%" + keyword + "%")
        );
    }

    public static Specification<NoticeJpaEntity> likeAuthorName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get("authorName"), "%" + keyword + "%"
        );
    }
}
