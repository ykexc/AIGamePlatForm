package com.kob.backend.task;

import com.kob.common.constant.Constant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author mqz
 */
@Component
public class RedisScheduled {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Scheduled(cron = "0 55 23 * * *")
    public void ClearRecords() {
        redisTemplate.delete(Constant.Redis.DAY_RANK);
    }

}
