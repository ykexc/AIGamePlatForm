package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;

/**
 * @author mqz
 */
public interface GetListService {
    List<Bot> getSnakeBotList();
    List<Bot> getGobangBotList();

    List<Bot> getBotList(Integer userId);
}
