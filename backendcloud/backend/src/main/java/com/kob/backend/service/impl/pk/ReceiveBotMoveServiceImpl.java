package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.gobang.GobangWebsocketServer;
import com.kob.backend.consumer.gobang.game.GobangGame;
import com.kob.backend.consumer.snake.WebSocketServer;
import com.kob.backend.consumer.snake.game.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

/**
 * @author mqz
 */
@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction, String container) {
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {  //此时为AI
                    game.setNextStepA(direction);
                    game.getPlayerA().setContainer(container);

                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                    game.getPlayerB().setContainer(container);
                }
            }
        }
        //  System.out.println(userId + " " + ":" + " " + direction);
        return "receive bot remove success";
    }

    @Override
    public String receiveBotMove(Integer userId, Integer nx, Integer ny, String container) {
        if (GobangWebsocketServer.users.get(userId) != null) {
            GobangGame game = GobangWebsocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.getPlayerA().setContainer(container);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.getPlayerB().setContainer(container);
                }
                game.setNxAndNy(nx, ny);
            }
        }
        //  System.out.println(userId + " " + ":" + " " + nx + " " + ny);
        return "receive bot remove success";
    }
}
