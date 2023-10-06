package com.kob.matchingsystem.listener;

import com.kob.matchingsystem.service.impl.util.MatchingPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * @author mqz
 */
@RabbitListener(queues = "match")
@Component
public class MatchingListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingListener.class);

    public static final MatchingPool MATCHING_POOL = new MatchingPool();


    @RabbitHandler
    public void sendMailMessage(Map<String, ? extends Serializable> data) {
        String type = (String) data.get("type");
        Integer userId = (Integer) data.get("user_id");
        String game = (String) data.get("game");
        if ("add".equals(type)) {
            Integer rating = (Integer) data.get("rating");
            Integer botId = (Integer) data.get("bot_id");
            MATCHING_POOL.addPlayer(userId, rating, botId, game);
            LOGGER.info("game: {} add player {}", game, userId);
        } else {
            LOGGER.info("game: {}, remove player {}", game, userId);
            MATCHING_POOL.removePlayer(userId, game);
        }
    }


}
