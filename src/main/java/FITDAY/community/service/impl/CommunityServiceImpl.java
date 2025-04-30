package FITDAY.community.service.impl;

import FITDAY.community.dto.request.CommunityRequestDto;
import FITDAY.community.dto.response.CommUpdateDto;
import FITDAY.community.dto.response.CommListDto;
import FITDAY.community.entity.Category;
import FITDAY.community.entity.Community;
import FITDAY.community.entity.QCategory;
import FITDAY.community.entity.QCommunity;
import FITDAY.community.exception.CategoryNotFoundException;
import FITDAY.community.exception.CommunityNotFoundException;
import FITDAY.community.repository.CategoryRepository;
import FITDAY.community.repository.CommunityRepository;
import FITDAY.community.service.CommunityService;
import FITDAY.redis.community.CommCheckCacheService;
import FITDAY.redis.community.CommCntCacheService;
import FITDAY.redis.community.CommFeedCacheService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final JPAQueryFactory jpaQueryFactory;

    private final CommCntCacheService cntCached;
    private final CommFeedCacheService feedCached;
    private final CommCheckCacheService checkCache;

    private final CategoryRepository categoryRepository;
    private final CommunityRepository communityRepository;

    private static final QCommunity qCommunity = QCommunity.community;
    private static final QCategory qCategory = QCategory.category;

    @Override
    @Transactional
    public ResponseEntity<CommUpdateDto> createCommunity(CommunityRequestDto requestDto) {

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryId()));

        Community community = Community.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(category)
                .build();

        Community save = communityRepository.save(community);

        cntCached.increment();

        CommUpdateDto body = new CommUpdateDto(
                save.getId(),
                save.getTitle(),
                save.getContent(),
                save.getCategory().getId(),
                save.getReadCnt(),
                save.getCreatedAt(),
                save.getUpdatedAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Override
    @Transactional
    public ResponseEntity<CommUpdateDto> updateCommunity(Long id, CommunityRequestDto requestDto) {

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityNotFoundException(id));

        community.setTitle(requestDto.getTitle());
        community.setContent(requestDto.getContent());

        if (!community.getCategory().getId().equals(requestDto.getCategoryId())) {
            Category newCat = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryId()));
            community.setCategory(newCat);
        }

        Community updated = communityRepository.save(community);

        CommUpdateDto body = new CommUpdateDto(
                updated.getId(),
                updated.getTitle(),
                updated.getContent(),
                updated.getCategory().getId(),
                updated.getReadCnt(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
        return ResponseEntity.ok(body);
    }

    @Override
    @Transactional
    public void deleteCommunity(Long id) {
        if (!communityRepository.existsById(id)) {
            throw new CommunityNotFoundException(id);
        }
        communityRepository.deleteById(id);
        feedCached.deleteComm(id);
        cntCached.decrement();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<CommListDto>> getHotTopN() {

        List<CommListDto> hotList = feedCached.getHot10();

        return ResponseEntity.ok(hotList);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<CommListDto>> getRecentTopN() {

        List<CommListDto> recentList = feedCached.getRecent10();

        return ResponseEntity.ok(recentList);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<CommListDto>> getCommunityList(Pageable pageable) {

        List<CommListDto> content = jpaQueryFactory
                .select(Projections.constructor(
                        CommListDto.class,
                        qCommunity.id,
                        qCommunity.title,
                        qCommunity.category.id.as("categoryId"),
                        qCommunity.readCnt,
                        qCommunity.createdAt
                ))
                .from(qCommunity)
                .join(qCommunity.category, qCategory)
                .orderBy(qCommunity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = cntCached.getCount();

        return ResponseEntity.ok(new PageImpl<>(content, pageable, total));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CommUpdateDto> getCommunity(Long id) {

        checkCache.incrementReadCnt(id);

        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CommunityNotFoundException(id));

        long readCnt = checkCache.getReadCnt(id);

        CommUpdateDto body = CommUpdateDto.builder()
                .id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .categoryId(community.getCategory().getId())
                .readCnt(readCnt)
                .createdAt(community.getCreatedAt())
                .updatedAt(community.getUpdatedAt())
                .build();

        return ResponseEntity.ok(body);
    }
}
