package com.seoulmilk.notice.infrastructure.persistence.repository;

import com.seoulmilk.notice.domain.entity.Notice;
import com.seoulmilk.notice.domain.repository.NoticeRepository;
import com.seoulmilk.notice.dto.request.UpdateNoticeRequest;
import com.seoulmilk.notice.exception.NoticeErrorCode;
import com.seoulmilk.notice.infrastructure.mapper.NoticeMapper;
import com.seoulmilk.notice.infrastructure.persistence.jpa.entity.NoticeJpaEntity;
import com.seoulmilk.notice.infrastructure.persistence.jpa.repository.NoticeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class NoticeRepositoryImpl implements NoticeRepository {

    private final NoticeMapper noticeMapper;
    private final NoticeJpaRepository noticeJpaRepository;

    @Override
    public Notice save(Notice notice) {
        NoticeJpaEntity noticeJpaEntity = noticeMapper.toJpaEntity(notice);
        if (noticeJpaEntity == null) {
            throw NoticeErrorCode.NOT_EXISTS_NOTICE.toException();
        }
        noticeJpaRepository.save(noticeJpaEntity);
        return noticeMapper.toDomainEntity(noticeJpaEntity);
    }

    @Override
    public Optional<Notice> findById(Long id) {
        return noticeJpaRepository.findById(id)
                .map(noticeMapper::toDomainEntity);
    }

    @Override
    public void delete(Notice notice) {
        noticeJpaRepository.delete(noticeMapper.toJpaEntity(notice));
    }

    @Override
    public Page<Notice> findAllOrderByIdDescAndEmpPk(Long empPk, Pageable pageable) {
        Page<NoticeJpaEntity> noticeJpaEntities = noticeJpaRepository.findAllOrderByIdDescAndId(pageable, empPk);
        return noticeJpaEntities.map(noticeMapper::toDomainEntity);
    }

    @Override
    public Page<Notice> findAllOrderByIdDesc(Pageable pageable) {
        Page<NoticeJpaEntity> noticeJpaEntities = noticeJpaRepository.findAllOrderByIdDesc(pageable);
        return noticeJpaEntities.map(noticeMapper::toDomainEntity);
    }

    @Override
    public Notice updateNotice(UpdateNoticeRequest updateNoticeRequest, String fileUrl) {
        Notice notice = noticeJpaRepository.findById(updateNoticeRequest.id())
                .map(noticeMapper::toDomainEntity)
                .orElseThrow(NoticeErrorCode.NOT_EXISTS_NOTICE::toException);
        Notice updatedNotice = notice.update(updateNoticeRequest.title(), updateNoticeRequest.content(), fileUrl);
        noticeJpaRepository.save(noticeMapper.toJpaEntity(updatedNotice));
        return updatedNotice;
    }

    @Override
    public Page<Notice> findAllByKeyword(Specification<NoticeJpaEntity> spec, Pageable pageable) {
        Page<NoticeJpaEntity> noticesByKeyword = noticeJpaRepository.findAll(spec, pageable);
        return noticesByKeyword.map(noticeMapper::toDomainEntity);
    }

    @Override
    public List<Notice> findAllByIds(List<Long> ids) {
        List<NoticeJpaEntity> noticeJpaEntities = noticeJpaRepository.findAllByIdIn(ids);
        return noticeJpaEntities.stream()
                .map(noticeMapper::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteAll(List<Notice> notices) {
        noticeJpaRepository.deleteAll(notices.stream()
                .map(noticeMapper::toJpaEntity)
                .toList());
    }
}
