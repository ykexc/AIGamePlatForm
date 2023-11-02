package com.kob.backend.consumer.snake.game;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.client.Api;
import com.kob.backend.consumer.snake.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.pojo.vo.DayRankVo;
import com.kob.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Game extends Thread {
    private Integer rows;
    private Integer cols;
    private Integer inner_walls_count;
    private int[][] g;
    private static final int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private Player playerA;
    private Player playerB;
    private static final String ADD_BOT_URL = "http://127.0.0.1:3002/bot/add/";

    private static Api api;
    private Integer nextStepA = null;

    private Integer nextStepB = null;

    private String status = "playing";

    private String loser = "";

    private final ReentrantLock lock = new ReentrantLock();

    private static RedisTemplate<String, Object> redisTemplate;

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }


    @Autowired
    public void setApi(Api api) {
        Game.api = api;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        Game.redisTemplate = redisTemplate;
    }

    public Game() {
    }

    public Game(Integer rows, Integer cols, Integer inner_walls_count,
                Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
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

        playerA = new Player(idA, botIdA, "none", languageA, botCodeA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, "none", languageB, botCodeB, 1, cols - 2, new ArrayList<>());
    }

    public int[][] getG() {
        return g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw())
                break;
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                judge();
                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else if (nextStepB == null) {
                        loser = "B";
                    }
                    //  System.out.println("loser is " + loser);
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }

    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            //  System.out.println("nextStepA = " + nextStepA);
            //  System.out.println("nextStepB = " + nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    private boolean checkIsValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell A = cellsA.get(n - 1);
        if (g[A.x][A.y] == 1) return false;
        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == A.x && cellsA.get(i).y == A.y) return false;
        }
        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == A.x && cellsB.get(i).y == A.y) return false;
        }
        return true;
    }

    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = checkIsValid(cellsA, cellsB);
        boolean validB = checkIsValid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finished";
            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    public void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        sendEndToOJ(playerA);
        sendEndToOJ(playerB);
        saveToDataBase();
        sendAllMessage(resp.toJSONString());

    }


    private void sendEndToOJ(Player player) {
        if (player.getBotId().equals(-1)) return;
        //  System.out.println("playerA" + playerA);
        //  System.out.println("playerB" + playerB);
        Map<String, String> req = new HashMap<String, String>() {{
            put("status", "2");
            put("game", "snake");
            put("container", player.getContainer());
        }};
        api.run(req);
    }


    public void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }


    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }


    private String getInput(Player player) {  //获取当前局面的状态
        Player me, you;
        if (player.getBotId().equals(playerA.getBotId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#"
               + me.getSx() + "#"
               + me.getSy() + "#("
               + me.getStepToString() + ")#"
               + you.getSx() + "#"
               + you.getSy() + "#("
               + you.getStepToString() + ")";

    }

    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) {  //人亲自出马
            return;
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        String status;
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));
        data.add("language", player.getLanguage());
        data.add("game", "snake");
        status = "none".equals(player.getContainer()) ? "0" : "1";
        data.add("container", player.getContainer());
        data.add("status", status);
        WebSocketServer.restTemplate.postForObject(ADD_BOT_URL, data, String.class);
    }

    private boolean nextStep() {
        try {
            Thread.sleep(200);  //frontend drawing time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void updateRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    public void saveToDataBase() {
        User userA = WebSocketServer.userMapper.selectById(playerA.getId());
        User userB = WebSocketServer.userMapper.selectById(playerB.getId());
        Integer ratingA = userA.getRating();
        Integer ratingB = userB.getRating();
        if ("A".equals(loser)) {
            ratingA -= 5;
            ratingB += 5;
        } else if ("B".equals(loser)) {
            ratingB += 5;
            ratingA -= 5;
        }
        DayRankVo a = new DayRankVo() {{
            setPhoto(userA.getPhoto());
            setUsername(userA.getUsername());
        }};
        DayRankVo b = new DayRankVo(){{
            setPhoto(userB.getPhoto());
            setUsername(userB.getUsername());
        }};
        redisTemplate.opsForZSet().add(Constant.Redis.DAY_RANK, a, ratingA + (1 - System.currentTimeMillis() * 1e-13));
        redisTemplate.opsForZSet().add(Constant.Redis.DAY_RANK, b, ratingB + (1 - System.currentTimeMillis() * 1e-13));
        updateRating(playerA, ratingA);
        updateRating(playerB, ratingB);
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepToString(),
                playerB.getStepToString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    public String getMapString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                stringBuilder.append(g[i][j]);
            }
        }
        return stringBuilder.toString();
    }
}
