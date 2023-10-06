package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author mqz
 */
@RestController
public class ReceiveBotMoveController {
    @Resource
    private ReceiveBotMoveService receiveBotMoveService;


    @PostMapping("/api/pk/game/snake/receiveBot")
    public String receiveSnakeBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        String container = Objects.requireNonNull(data.getFirst("container"));
        return receiveBotMoveService.receiveBotMove(userId, direction, container);
    }

    @PostMapping("/api/pk/game/gobang/receiveBot")
    public String receiveGobangBotMove(@RequestParam MultiValueMap<String, String> data) {
        //  System.out.println(data);
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("userId")));
        String snx = Objects.requireNonNull(data.getFirst("nx"));
        String sny = Objects.requireNonNull(data.getFirst("ny"));
        Integer nx = Integer.parseInt(Objects.requireNonNull(snx.trim()));
        Integer ny = Integer.parseInt(Objects.requireNonNull(sny.trim()));
        String container = Objects.requireNonNull(data.getFirst("container"));
        //  System.out.println(nx + ": " + ny + " " + container);
        return receiveBotMoveService.receiveBotMove(userId, nx, ny, container);
    }
}
