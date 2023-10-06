package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @author mqz
 */
public interface RegisterService {
    Map<String, String> register (String username, String password, String confirmPassword);
}
