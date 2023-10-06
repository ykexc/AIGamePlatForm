package com.kob.backend.controller.pk;


import com.kob.backend.service.pk.StartGameService;
import com.kob.common.req.Couple;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mqz
 */
@RestController
public class StartGameController {
    @Resource
    private StartGameService startGameService;

    @PostMapping("/pk/snake/start/game/")
    public String startSnakeGame(@RequestBody Couple couple) {
        return startGameService.startSnakeGame(couple.getaId(), couple.getaBotId(), couple.getbId(), couple.getbBotId());
    }


    @PostMapping("/pk/gobang/start/game")
    public String startGobangGame(@RequestBody Couple couple) {
        return startGameService.startGobangGame(couple.getaId(), couple.getaBotId(), couple.getbId(), couple.getbBotId());
    }

}
