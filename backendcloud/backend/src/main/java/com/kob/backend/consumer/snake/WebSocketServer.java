package com.kob.backend.consumer.snake;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.snake.game.Game;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.utils.JwtAuthentication;
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

@Component
@ServerEndpoint("/websocket/snake/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    public static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private User user;
    private Session session;

    public static UserMapper userMapper;

    public static RecordMapper recordMapper;

    private static BotMapper botMapper;

    public static RestTemplate restTemplate;

    private static RabbitTemplate rabbitTemplate;

    public Game game;


    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        WebSocketServer.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        //  System.out.println("connected" + " " + userId);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        //  System.out.println("close");
        if (user != null) {
            users.remove(this.user.getId());
        }
    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1)) {  //botId为-1时表示真人
                game.setNextStepA(direction);
            }

        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        //  System.out.println("get message");
        JSONObject jsonObject = JSONObject.parseObject(message);
        String event = jsonObject.getString("event");
        if ("start-matching".equals(event)) {
            startMatching(jsonObject.getInteger("bot_id"));
            //  System.out.println(jsonObject.containsKey("bot_id"));
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(jsonObject.getInteger("direction"));
        }
    }


    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);
        Game game = new Game(13, 14, 20, a.getId(), botA, b.getId(), botB);
        game.createMap();
        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("aBotId", aBotId);
        respA.put("bBotId", bBotId);
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());
        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("game", respGame);
        respB.put("aBotId", aBotId);
        respB.put("bBotId", bBotId);
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        if (users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching(Integer botId) {
        //  System.out.println("start matching");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", this.user.getId());
        data.put("rating", this.user.getRating());
        data.put("bot_id", botId);
        data.put("game", com.kob.common.enums.Game.SNAKE.getName());
        data.put("type", "add");
//        restTemplate.postForObject(addPlayerUrl, data, String.class);
        rabbitTemplate.convertAndSend(MATCH, data);
    }

    private void stopMatching() {
        //  System.out.println("stop matching");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", this.user.getId());
        data.put("game", com.kob.common.enums.Game.SNAKE.getName());
        data.put("type", "remove");
//        restTemplate.postForObject(removePlayerUrl, data, String.class);
        rabbitTemplate.convertAndSend(MATCH, data);
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
}
