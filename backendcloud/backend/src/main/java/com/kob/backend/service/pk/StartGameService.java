package com.kob.backend.service.pk;

/**
 * @author mqz
 */
public interface StartGameService {

    String startSnakeGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);

    String startGobangGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);
}
