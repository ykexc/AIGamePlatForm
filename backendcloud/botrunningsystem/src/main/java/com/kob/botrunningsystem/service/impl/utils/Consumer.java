package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.client.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author mqz
 */
@Component
@Slf4j
public class Consumer extends Thread {

    private Bot bot;
    private static Api api;

    @Autowired
    public void setApi(Api api) {
        Consumer.api = api;
    }

    public void startTimeOut(long timeOut, Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }

    private String getRandom() {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public void run() {
        HashMap<String, String> req = new HashMap<String, String>(){{
            put("status", bot.getStatus());
            put("container", bot.getContainer());
            put("game", bot.getGame());
            put("random", getRandom());
            put("codes", bot.getBotCode());
            put("inputs", bot.getInput());
            put("language", bot.getLanguage());
            put("userId", bot.getUserId().toString());
        }};
        Map<String, String> resp = api.run(req);
        if ("ok".equals(resp.get("result"))) log.info("--------" + "running ok" + "-----------");
    }
}
