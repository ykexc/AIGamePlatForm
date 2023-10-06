package com.kob.backend.service.user.account.qq;

import com.alibaba.fastjson.JSONObject;

/**
 * @author mqz
 */
public interface QQWebService {


    JSONObject applyCode();

    JSONObject receiveCode(String state, String code);

}
