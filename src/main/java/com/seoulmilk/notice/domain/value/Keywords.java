package com.seoulmilk.notice.domain.value;

import com.seoulmilk.notice.exception.NoticeErrorCode;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import com.seoulmilk.notice.infrastructure.persistence.jpa.specification.NoticeSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
public enum Keywords {
    TITLE_AND_CONTENT("title_and_content") {
        @Override
        public Specification<NoticeJpaEntity> getSpecification(String keyword) {
            return NoticeSpecifications.likeTitleAndContent(keyword);
        }
    },
    AUTHOR("author") {
        @Override
        public Specification<NoticeJpaEntity> getSpecification(String keyword) {
            return NoticeSpecifications.likeAuthorName(keyword);
        }
    },
    ALL("all") {
        @Override
        public Specification<NoticeJpaEntity> getSpecification(String keyword) {
            return NoticeSpecifications.likeAll(keyword);
        }
    };

    private final String value;

    Keywords(String value) {
        this.value = value;
    }

    public abstract Specification<NoticeJpaEntity> getSpecification(String keyword);

    public static Keywords fromValue(String value) {
        for (Keywords type : Keywords.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw NoticeErrorCode.INVALID_SEARCH_KEYWORD.toException();
    }
}
