package FITDAY.redis.auth;

import FITDAY.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshCacheService {

    private final JwtProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    public void saveRefreshToken(String redisKey, String refreshToken) {

        long expiration = jwtProvider.getRefreshDuration();

        redisTemplate.opsForValue()
                .set(redisKey, refreshToken, Duration.ofMillis(expiration));
    }
}
