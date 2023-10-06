package com.kob.backend.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author mqz
 */
@Component
@RequiredArgsConstructor
public class LimitUtil {

    private final StringRedisTemplate redisTemplate;


    private interface LimitAction {
        boolean run(boolean overLock);
    }


    public boolean limitPeriodCheck(String countKey, String blockKey, int blockTime, int freq, int period) {
        return this.internalCheck(countKey, freq, period, isExceed -> {
           if (isExceed) redisTemplate.opsForValue().set(blockKey, "", blockTime, TimeUnit.MINUTES);
           return !isExceed;
        });
    }


    private boolean internalCheck(String countKey, int freq, int period, LimitAction action) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(countKey))) {
            Long cnts = Optional.ofNullable(redisTemplate.opsForValue().increment(countKey)).orElse(0L);
            return action.run(cnts > freq);
        } else {
            redisTemplate.opsForValue().set(countKey, "1", period, TimeUnit.SECONDS);
            return true;
        }
    }



}
