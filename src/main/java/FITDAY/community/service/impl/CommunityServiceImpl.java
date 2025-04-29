package FITDAY.community.service.impl;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommunityResponseDto;
import FITDAY.community.entity.QCategory;
import FITDAY.community.entity.QCommunity;
import FITDAY.community.service.CommunityService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final JPAQueryFactory jpaQueryFactory;
    private final QCommunity qCommunity = QCommunity.community;
    private final QCategory qCategory = QCategory.category;

    @Override
    public ResponseEntity<CommunityResponseDto> createCommunity(CommunityRequestDto requestDto) {

        return null;
    }

    @Override
    public ResponseEntity<CommunityResponseDto> updateCommunity(Long id, CommunityRequestDto requestDto) {

        return null;
    }


    @Override
    public void deleteCommunity(Long id) {


    }

    @Override
    public ResponseEntity<List<CommunityResponseDto>> getHotTopN() {

        return null;
    }

    @Override
    public ResponseEntity<List<CommunityResponseDto>> getRecentTopN() {

        return null;
    }

    @Override
    public ResponseEntity<Page<CommunityResponseDto>> getCommunityList(Pageable pageable) {

        List<CommunityResponseDto> content = jpaQueryFactory
                .select(Projections.constructor(
                        CommunityResponseDto.class,
                        qCommunity.id,
                        qCommunity.title,
                        qCommunity.category.id
                ))
                .from(qCommunity)
                .join(qCommunity.category, qCategory)
                .orderBy(qCommunity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(qCommunity.id.count())
                .from(qCommunity)
                .fetchOne();

        return ResponseEntity.ok(new PageImpl<>(content, pageable, total));
    }
}
