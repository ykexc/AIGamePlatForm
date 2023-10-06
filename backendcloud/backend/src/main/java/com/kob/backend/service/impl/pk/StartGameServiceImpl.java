package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.gobang.GobangWebsocketServer;
import com.kob.backend.consumer.snake.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @author mqz
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startSnakeGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        //  System.out.println("startGame" + " " + aId + " " +bId);
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "startSnakeGame";
    }

    @Override
    public String startGobangGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        //  System.out.println("startGame" + " " + aId + " " +bId);
        GobangWebsocketServer.startGame(aId, aBotId, bId, bBotId);
        return "startGobangGame";
    }
}
