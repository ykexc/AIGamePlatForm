package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author mqz
 */
public interface InfoService {

    Map<String, String> getInfo();

    Map<String, String> updatePhoto(Integer userId, String url);
}
