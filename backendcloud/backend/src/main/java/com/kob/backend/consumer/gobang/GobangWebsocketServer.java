package com.kob.backend.consumer.gobang;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.gobang.game.GobangGame;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.JwtAuthentication;
import com.kob.common.enums.Game;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.kob.common.constant.Constant.Rabbit.MATCH;

/**
 * @author mqz
 */
@Component
@ServerEndpoint("/websocket/gobang/{token}")
public class GobangWebsocketServer {

    public static final ConcurrentHashMap<Integer, GobangWebsocketServer> users = new ConcurrentHashMap<>();

    public static UserMapper userMapper;

    public static RecordMapper recordMapper;

    private static BotMapper botMapper;

    private Session session;

    private User user;

    public GobangGame game;

    public static RestTemplate restTemplate;

    private static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {GobangWebsocketServer.rabbitTemplate = rabbitTemplate;}

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        GobangWebsocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        GobangWebsocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        GobangWebsocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        GobangWebsocketServer.restTemplate = restTemplate;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
        } else {
            session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject event = JSONObject.parseObject(message);
        String e = event.getString("event");
        switch (e) {
            case "start-matching": {
                startMatching(event.getInteger("bot_id"));
                break;
            }
            case "stop-matching": {
                stopMatching();
                break;
            }
            case "play": {
                Integer nx = event.getInteger("nx"), ny = event.getInteger("ny");
                String nowRound = event.getString("now_round");
                play(nx, ny, nowRound);
                break;
            }
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void startMatching(Integer botId) {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", this.user.getId());
        data.put("rating", this.user.getRating());
        data.put("bot_id", botId);
        data.put("game", Game.GOBANG.getName());
        data.put("type", "add");
        rabbitTemplate.convertAndSend(MATCH, data);
    }


    private void stopMatching() {
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", this.user.getId());
        data.put("game", Game.GOBANG.getName());
        data.put("type", "remove");
        rabbitTemplate.convertAndSend(MATCH, data);
    }


    public void play(Integer nx, Integer ny, String nowRound) {
        if ("A".equals(nowRound) && this.game.getRound().equals(nowRound) && game.getPlayerA().getBotId().equals(-1))
            this.game.setNxAndNy(nx, ny);
        if ("B".equals(nowRound) && this.game.getRound().equals(nowRound) && game.getPlayerB().getBotId().equals(-1))
            this.game.setNxAndNy(nx, ny);
    }


    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User userA = userMapper.selectById(aId), userB = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);
        GobangGame game = new GobangGame(aId, bId, botA, botB);
        if (userA == null || userB == null) return;
        if (users.get(aId) == null || users.get(bId) == null) return;
        users.get(aId).game = game;
        users.get(bId).game = game;
        String event = "start-matching";
        Integer[][] gamemap = game.getGamemap();
        String round = game.getRound();
        game.start();
        JSONObject respGame = new JSONObject() {{
            put("gamemap", gamemap);
            put("aId", aId);
            put("bId", bId);
            put("round", round);
        }};

        JSONObject respA = new JSONObject() {{
            put("event", event);
            put("game", respGame);
            put("aBotId", aBotId);
            put("bBotId", bBotId);
            put("opponent_username", userB.getUsername());
            put("opponent_photo", userB.getPhoto());
        }};
        if (users.get(aId) != null)
            users.get(aId).sendMessage(respA.toJSONString());
        JSONObject respB = new JSONObject() {{
            put("event", event);
            put("game", respGame);
            put("aBotId", aBotId);
            put("bBotId", bBotId);
            put("opponent_username", userA.getUsername());
            put("opponent_photo", userA.getPhoto());
        }};
        if (users.get(bId) != null)
            users.get(bId).sendMessage(respB.toJSONString());
    }


}
