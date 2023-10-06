package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.GetListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mqz
 */

@RestController
@RequiredArgsConstructor
public class GetListController {

    private final GetListService getListService;

    @GetMapping("/api/user/bot/snake/getlist/")
    public List<Bot> getSnakeBotList() {
        return getListService.getSnakeBotList();
    }


    @GetMapping("/api/user/bot/gobang/getlist/")
    public List<Bot> getGobangBotList() {
        return getListService.getGobangBotList();
    }


    @GetMapping("/api/user/bot/getlist/")
    public List<Bot> getList(@RequestAttribute Integer userId) {
        return getListService.getBotList(userId);
    }
}
