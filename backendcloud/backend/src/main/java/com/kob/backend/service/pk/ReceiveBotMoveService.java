package com.kob.backend.service.pk;

/**
 * @author mqz
 */
public interface ReceiveBotMoveService {

    String receiveBotMove(Integer userId, Integer direction, String container);

    String receiveBotMove(Integer userId, Integer nx, Integer ny, String container);

}
