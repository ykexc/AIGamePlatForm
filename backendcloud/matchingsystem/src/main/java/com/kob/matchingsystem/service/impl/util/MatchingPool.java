package com.kob.matchingsystem.service.impl.util;

import com.kob.common.req.Couple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mqz
 */
@Component
public class MatchingPool extends Thread {
    private static RestTemplate restTemplate;
    private static List<Player> snakePlayers = new ArrayList<>();

    private static List<Player> gobangPlayers = new ArrayList<>();
    private final ReentrantLock snakeLock = new ReentrantLock();

    private final ReentrantLock gobangLock = new ReentrantLock();

    public static final String START_SNAKE_GAME_URL = "http://127.0.0.1:3000/pk/snake/start/game/";

    public static final String START_GOBANG_GAME_URL = "http://127.0.0.1:3000/pk/gobang/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                snakeLock.lock();
                try {
                    matchSnakePlayers();
                } finally {
                    snakeLock.unlock();
                }
                gobangLock.lock();
                try {
                    matchGobangPlayers();
                } finally {
                    gobangLock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    protected void matchSnakePlayers() {
        int persons = snakePlayers.size();
        boolean[] used = new boolean[persons];
        for (int i = 0; i < persons; i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < persons; j++) {
                if (used[j]) continue;
                Player a = snakePlayers.get(i), b = snakePlayers.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b, "snake");
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < persons; i++) {
            if (used[i]) continue;
            newPlayers.add(snakePlayers.get(i));
        }
        snakePlayers = newPlayers;
        increaseWaitingTime();
    }


    public void matchGobangPlayers() {
        int persons = gobangPlayers.size();
        boolean[] used = new boolean[persons];
        for (int i = 0; i < persons; i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < persons; j++) {
                if (used[j]) continue;
                Player a = gobangPlayers.get(i), b = gobangPlayers.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b, "gobang");
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < persons; i++) {
            if (used[i]) continue;
            newPlayers.add(gobangPlayers.get(i));
        }
        gobangPlayers = newPlayers;
        increaseWaitingTime();
    }

    private boolean checkMatched(Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void sendResult(Player a, Player b, String game) {
        Couple data = new Couple();
        data.setaId(a.getUserId());
        data.setbId(b.getUserId());
        data.setaBotId(a.getBotId());
        data.setbBotId(b.getBotId());;
        if ("snake".equals(game)) restTemplate.postForObject(START_SNAKE_GAME_URL, data, String.class);
        if ("gobang".equals(game)) restTemplate.postForObject(START_GOBANG_GAME_URL, data, String.class);
    }

    private void increaseWaitingTime() {  //所有玩家等待时间+1
        for (Player player : snakePlayers) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
        for (Player player : gobangPlayers) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    public void addPlayer(Integer userId, Integer rating, Integer botId, String gameName) {
        if ("snake".equals(gameName)) {
            snakeLock.lock();
            try {
                snakePlayers.add(new Player(userId, rating, botId, 0));
            } finally {
                snakeLock.unlock();
            }
        }
        if ("gobang".equals(gameName)) {
            gobangLock.lock();
            try {
                gobangPlayers.add(new Player(userId, rating, botId, 0));
            } finally {
                gobangLock.unlock();
            }
        }
    }

    public void removePlayer(Integer userId, String gameName) {

        if ("snake".equals(gameName)) {
            snakeLock.lock();
            try {
                List<Player> newPlayers = new ArrayList<>();
                for (Player player : snakePlayers) {
                    if (!player.getUserId().equals(userId)) newPlayers.add(player);
                }
                snakePlayers = newPlayers;
            } finally {
                snakeLock.unlock();
            }
        }

        if ("gobang".equals(gameName)) {
            gobangLock.lock();
            try {
                List<Player> newPlayers = new ArrayList<>();
                for (Player player : gobangPlayers) {
                    if (!player.getUserId().equals(userId)) newPlayers.add(player);
                }
                gobangPlayers = newPlayers;
            } finally {
                gobangLock.unlock();
            }
        }
    }

}
