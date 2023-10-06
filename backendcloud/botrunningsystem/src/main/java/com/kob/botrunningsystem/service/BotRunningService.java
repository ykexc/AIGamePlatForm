package com.kob.botrunningsystem.service;

/**
 * @author mqz
 */
public interface BotRunningService {

    String addBot(Integer userId, String botCode, String input, String language, String game, String container, String status);

}
