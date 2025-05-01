package FITDAY.redis.community;

import FITDAY.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommCheckCacheService {

    private static final String READ_HASH = "delta:reads";

    @Qualifier("hash")
    private final RedisTemplate<String, Long> redisTemplate;
    private final CommunityRepository communityRepository;

    public void incrementReadCnt(Long communityId) {

        redisTemplate.opsForHash().increment(READ_HASH, communityId.toString(), 1L);
    }

    public long getReadCnt(Long communityId) {

        Long delta = (Long) redisTemplate.opsForHash().get(READ_HASH, communityId.toString());
        long dbCnt = communityRepository.findReadCntById(communityId);

        return dbCnt + (delta != null ? delta : 0L);
    }

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    public void flush() {

        Map<Object, Object> readDelta = redisTemplate.opsForHash().entries(READ_HASH);

        flushReadCnt(readDelta);

        redisTemplate.delete(READ_HASH);
    }

    private void flushReadCnt(Map<Object, Object> delta) {

        if (delta == null) return;

        delta.forEach((k, v) -> {
            String key = k.toString();
            String val = v.toString();
            long id = Long.parseLong(key);
            long dt = Long.parseLong(val);
            if (dt > 0) {
                communityRepository.incrementReadCount(id, dt);
            }
        });
    }
}
