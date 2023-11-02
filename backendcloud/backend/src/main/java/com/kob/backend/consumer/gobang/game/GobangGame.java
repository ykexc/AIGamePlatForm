package com.kob.backend.consumer.gobang.game;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.client.Api;
import com.kob.backend.consumer.gobang.GobangWebsocketServer;
import com.kob.backend.consumer.snake.game.Game;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.pojo.vo.DayRankVo;
import com.kob.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mqz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Component
public class GobangGame extends Thread {

    Integer[][] gamemap;  //在目前这种策略下,可有可无

    String round;  //白棋先走, A为白棋

    GobangPlayer playerA;

    GobangPlayer playerB;

    Integer nx, ny;

    private static final String ADD_BOT_URL = "http://127.0.0.1:3002/bot/add/";

    String loser;

    private final ReentrantLock lock = new ReentrantLock();


    private static Api api;

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setApi(Api api) {
        GobangGame.api = api;
    }


    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        GobangGame.redisTemplate = redisTemplate;
    }

    public GobangGame(Integer userAId, Integer userBId, Bot botA, Bot botB) {
        gamemap = new Integer[16][16];

        round = "A";

        loser = "none";

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        String languageA = "", languageB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
            languageA = botA.getType();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
            languageB = botB.getType();
        }

        playerA = new GobangPlayer(userAId, botIdA, new LinkedHashSet<>(), "none", languageA, botCodeA);
        playerB = new GobangPlayer(userBId, botIdB, new LinkedHashSet<>(), "none", languageB, botCodeB);
    }


    public void sendPlay() {
        JSONObject jsonObject = new JSONObject();
        try {
            lock.lock();
            jsonObject.put("event", "play");
            jsonObject.put("round", round);
            jsonObject.put("nx", nx);
            jsonObject.put("ny", ny);
            sendAllMessage(jsonObject.toJSONString());
//            System.out.println("发送前端 " + "nx: " + nx + "  ny: " + ny + " " + round);
            nx = null;
            ny = null;
        } finally {
            lock.unlock();
        }
    }

    public void sendAllMessage(String message) {
        if (GobangWebsocketServer.users.get(playerA.getId()) != null)
            GobangWebsocketServer.users.get(playerA.getId()).sendMessage(message);
        if (GobangWebsocketServer.users.get(playerB.getId()) != null)
            GobangWebsocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    public boolean nextIdx() {

        try {
            Thread.sleep(200);  //frontend drawing time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if ("A".equals(round)) {
            sendBotCode(playerA);
        } else {
            sendBotCode(playerB);
        }


        for (int i = 0; i < 600; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                lock.lock();
                if (nx != null && ny != null) {
                    if ("A".equals(round)) playerA.getPoints().add(nx + "#" + ny);
                    else playerB.getPoints().add(nx + "#" + ny);
                    return true;
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    private void sendBotCode(GobangPlayer player) {
        if (player.getBotId().equals(-1))  //为真人
            return;
        MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>() {{
            add("user_id", player.getId().toString());
            add("bot_code", player.getBotCode());
            add("input", getInput(player));
            add("language", player.getLanguage());
            add("game", "gobang");
            add("status", "none".equals(player.getContainer()) ? "0" : "1");
            add("container", player.getContainer());
        }};
        GobangWebsocketServer.restTemplate.postForObject(ADD_BOT_URL, data, String.class);
    }


    private String getInput(GobangPlayer player) {
        GobangPlayer rival = (player == playerA ? playerB : playerA);
        LinkedHashSet<String> myPoints = player.getPoints();
        LinkedHashSet<String> rivalPoints = rival.getPoints();
        String mySteps = String.join("*", myPoints);
        String rivalSteps = String.join("*", rivalPoints);
        if (mySteps.isEmpty()) mySteps = "*";
        if (rivalSteps.isEmpty()) rivalSteps = "*";
        return mySteps + "\n" + rivalSteps;
    }


    public boolean judge() {
        //相对于暴力判断的优化方法,只需要判断当前这颗棋子的上下左右和斜线判断
        LinkedHashSet<String> set = "A".equals(round) ? playerA.getPoints() : playerB.getPoints();
        int nX = nx, nY = ny;
        int leftAndRight = 0, upAndDown = 0, leftSlash = 0, rightSlash = 0;
        for (int i = nY - 1; i > 0; i--)
            if (set.contains(String.valueOf(nX) + '#' + i)) leftAndRight++;
            else break;
        for (int i = nY + 1; i < 16; i++)
            if (set.contains(String.valueOf(nX) + '#' + i)) leftAndRight++;
            else break;
        if (leftAndRight >= 4) return true;

        for (int i = nX - 1; i > 0; i--)
            if (set.contains(String.valueOf(i) + '#' + nY)) upAndDown++;
            else break;
        for (int i = nX + 1; i < 16; i++)
            if (set.contains(String.valueOf(i) + '#' + nY)) upAndDown++;
            else break;
        if (upAndDown >= 4) return true;

        for (int i = nX - 1, j = nY - 1; i > 0 && j > 0; i--, j--)
            if (set.contains(String.valueOf(i) + '#' + j)) leftSlash++;
            else break;
        for (int i = nX + 1, j = nY + 1; i < 16 && j < 16; i++, j++)
            if (set.contains(String.valueOf(i) + '#' + j)) leftSlash++;
            else break;
        if (leftSlash >= 4) return true;

        for (int i = nX - 1, j = nY + 1; i > 0 && j < 16; i--, j++)
            if (set.contains(String.valueOf(i) + '#' + j)) rightSlash++;
            else break;
        for (int i = nX + 1, j = nY - 1; i < 16 && j > 0; i++, j--)
            if (set.contains(String.valueOf(i) + '#' + j)) rightSlash++;
            else break;
        return rightSlash >= 4;
    }

    public void sendResult() {
        JSONObject jsonObject = new JSONObject() {{
            put("event", "result");
            put("loser", loser);
            put("round", round);  //结束时处于哪一回合
            put("nx", nx);
            put("ny", ny);
            put("aBotId", playerA.getBotId());
            put("bBotId", playerB.getBotId());
        }};
        sendAllMessage(jsonObject.toJSONString());
        sendEndToOJ(playerA);
        sendEndToOJ(playerB);
        saveToDataBase();
    }

    public void sendTimeOutResult() {
        JSONObject jsonObject = new JSONObject() {{
            put("event", "timeoutResult");
            put("loser", loser);
            put("aBotId", playerA.getBotId());
            put("bBotId", playerB.getBotId());
        }};
        sendAllMessage(jsonObject.toJSONString());
        sendEndToOJ(playerA);
        sendEndToOJ(playerB);
        saveToDataBase();
    }


    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextIdx()) {
                if (judge()) {
                    loser = "A".equals(round) ? "B" : "A";
                    sendResult();
                    break;
                } else {
                    round = "A".equals(round) ? "B" : "A";
                    sendPlay();  //这里直接发送已经改变后的round,因为前端已经保存了(上)回合信息,交给前端逻辑去处理
                }
            } else {
                try {
                    lock.lock();
                    loser = round;
                    sendTimeOutResult();
                } finally {
                    lock.unlock();
                }
                break;
            }
        }
    }


    public void saveToDataBase() {
        Integer AId = playerA.getId(), BId = playerB.getId();
        User userA = GobangWebsocketServer.userMapper.selectById(AId);
        User userB = GobangWebsocketServer.userMapper.selectById(BId);
        Integer ratingA = userA.getRating(), ratingB = userB.getRating();
        DayRankVo a = new DayRankVo(), b = new DayRankVo();
        a.setUsername(userA.getUsername());
        a.setPhoto(userA.getPhoto());
        b.setUsername(userB.getUsername());
        b.setPhoto(userB.getPhoto());
        if ("A".equals(loser)) {
            redisTemplate.opsForZSet().add(Constant.Redis.DAY_RANK, a,
                    (ratingA - 5) + (1 - System.currentTimeMillis() * 1e-13));
            redisTemplate.opsForZSet().add(Constant.Redis.DAY_RANK, b,
                    (ratingB + 5) + (1 - System.currentTimeMillis() * 1e-13));
            userA.setRating(ratingA - 5);
            userB.setRating(ratingB + 5);
        } else {
            redisTemplate.opsForZSet().add(Constant.Redis.DAY_RANK, a,
                    (ratingA + 5) + (1 - System.currentTimeMillis() * 1e-13));
            redisTemplate.opsForZSet().add(Constant.Redis.DAY_RANK, b,
                    (ratingB - 5) + (1 - System.currentTimeMillis() * 1e-13));
            userA.setRating(ratingA + 5);
            userB.setRating(ratingB - 5);
        }

        GobangWebsocketServer.userMapper.updateById(userA);
        GobangWebsocketServer.userMapper.updateById(userB);
        String aSteps = String.join("*", playerA.getPoints());
        String bSteps = String.join("*", playerB.getPoints());
        Record record = new Record(
                null,
                AId,
                null,
                null,
                BId,
                null,
                null,
                aSteps,
                bSteps,
                null,
                loser,
                new Date()
        );
        GobangWebsocketServer.recordMapper.insert(record);
    }


    public void setNxAndNy(Integer nx, Integer ny) {
        try {
            lock.lock();
            if (checkValid(nx, ny)) return;
            this.nx = nx;
            this.ny = ny;
        } finally {
            lock.unlock();
        }
    }


    public boolean checkValid(Integer nx, Integer ny) {
        return playerA.getPoints().contains(nx + "#" + ny) || playerB.getPoints().contains(nx + "#" + ny);
    }




    private void sendEndToOJ(GobangPlayer player) {
        if (player.getBotId().equals(-1)) return;
        //  System.out.println("playerA" + playerA);
        //  System.out.println("playerB" + playerB);
        Map<String, String> req = new HashMap<String, String>(){{
            put("status", "2");
            put("game", "gobang");
            put("container", player.getContainer());
        }};
        api.run(req);
    }
}
