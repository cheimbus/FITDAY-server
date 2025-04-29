package FITDAY.redis.community;

import FITDAY.community.repository.CommunityRepository;
import FITDAY.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CountCacheService {

    private static final String KEY = "community-count";
    private final RedisTemplate<String, Long> redisTemplate;
    private final CommunityRepository communityRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void warmup() {
        Long count = communityRepository.count();
        redisTemplate.opsForValue().set(KEY, count, 1, TimeUnit.DAYS);
    }

    public long getCount() {
        Long cached = redisTemplate.opsForValue().get(KEY);
        if(cached != null) {
            return cached;
        }
        long cnt = communityRepository.count();
        redisTemplate.opsForValue().set(KEY, cnt, 1, TimeUnit.DAYS);
        return cnt;
    }

    public void increment() {
        redisTemplate.opsForValue().increment(KEY, 1);
    }

    public void decrement() {
        redisTemplate.opsForValue().decrement(KEY, 1);
    }
}
