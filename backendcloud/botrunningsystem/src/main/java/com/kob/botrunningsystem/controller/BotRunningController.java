package com.kob.botrunningsystem.controller;

import com.kob.botrunningsystem.service.BotRunningService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author mqz
 */
@RestController
public class BotRunningController {
    @Resource
    BotRunningService botRunningService;

    @PostMapping("/bot/add/")
    public String addBot(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String botCode = Objects.requireNonNull(data.getFirst("bot_code"));
        String input = Objects.requireNonNull(data.getFirst("input"));
        String language = Objects.requireNonNull(data.getFirst("language"));
        String game = Objects.requireNonNull(data.getFirst("game"));
        String container = Objects.requireNonNull(data.getFirst("container"));
        String status = Objects.requireNonNull(data.getFirst("status"));
        return botRunningService.addBot(userId, botCode, input, language, game, container, status);
    }

}
