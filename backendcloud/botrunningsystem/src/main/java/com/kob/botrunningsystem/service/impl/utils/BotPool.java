package com.kob.botrunningsystem.service.impl.utils;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author mqz
 */
public class BotPool extends Thread {

    private static final BlockingDeque<Bot> bots = new LinkedBlockingDeque<>();

    public void addBot(Integer userId, String botCode, String input, String language, String game, String container, String status) {
        try {
            bots.putLast(new Bot(userId, botCode, input, language, game, container, status));
        } catch (InterruptedException ignore) {
        }

    }


    @Override
    public void run() {
        while (true) {
            try {
                Bot bot = bots.takeFirst();
                consume(bot);
            } catch (InterruptedException e) {
                break;
            }

        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeOut(5000, bot);
    }

}

