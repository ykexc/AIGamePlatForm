package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author mqz
 */
public interface LoginService {

    public Map<String, String> getToken (String username, String password);
}
