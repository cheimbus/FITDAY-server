package FITDAY.redis.community;

import FITDAY.community.dto.response.CommListDto;
import FITDAY.community.entity.QCategory;
import FITDAY.community.entity.QCommunity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommFeedCacheService {

    private final JPAQueryFactory queryFactory;

    private static final String HOT_KEY    = "community:hot10";
    private static final String RECENT_KEY = "community:recent10";
    private static final int LIMIT = 10;
    private static final QCommunity qCommunity = QCommunity.community;
    private static final QCategory qCategory = QCategory.category;

    private final RedisTemplate<String, List<CommListDto>> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void warmup() {

        refreshCache();
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void refreshCache() {

        cacheHot10();
        cacheRecent10();
    }

    private void cacheHot10() {

        List<CommListDto> hot10 = queryFactory
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
                .orderBy(qCommunity.readCnt.desc())
                .limit(LIMIT)
                .fetch();

        redisTemplate.opsForValue().set(HOT_KEY, hot10);
    }

    private void cacheRecent10() {

        List<CommListDto> recent10 = queryFactory
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
                .orderBy(qCommunity.createdAt.desc())
                .limit(LIMIT)
                .fetch();
        redisTemplate.opsForValue().set(RECENT_KEY, recent10);
    }

    public void deleteComm(Long communityId) {

        removeFromList(HOT_KEY, communityId);
        removeFromList(RECENT_KEY, communityId);
    }

    private void removeFromList(String key, Long id) {

        List<CommListDto> list = redisTemplate.opsForValue().get(key);
        if (list == null || list.isEmpty()) return;

        List<CommListDto> filtered = list.stream()
                .filter(dto -> !dto.getId().equals(id))
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, filtered);
    }

    public List<CommListDto> getHot10() {

        return redisTemplate.opsForValue().get(HOT_KEY);
    }

    public List<CommListDto> getRecent10() {

        return redisTemplate.opsForValue().get(RECENT_KEY);
    }
}
