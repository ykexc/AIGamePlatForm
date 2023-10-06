package com.kob.backend.service.impl.user.account;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.qq.QQWebService;
import com.kob.backend.utils.HttpClientUtil;
import com.kob.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author mqz
 */
@Service
@RequiredArgsConstructor
public class QQWebServiceImpl implements QQWebService {

    @Value(value = "${qq.appid}")
    private String appId;
    @Value(value = "${qq.appkey}")
    private String appKey;


    private final UserMapper userMapper;

    private final StringRedisTemplate stringRedisTemplate;


    private static final String AUTH_URI = "https://graph.qq.com/oauth2.0/authorize";

    private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    private static final String OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me";

    private static final String GET_INFO_URL = "https://graph.qq.com/user/get_user_info";

    private static final String REDIRECT_URI = "https://ykexc.work/user/account/qq/receive_code";


    private final static Random RANDOM = new Random();

    @Override
    public JSONObject applyCode() {

        JSONObject jsonObject = new JSONObject();

        String encodeUrl;

        try {
            encodeUrl = URLEncoder.encode(REDIRECT_URI, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            jsonObject.put("result", "failed");
            return jsonObject;
        }


        //随机字符串，防止csrf攻击
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            state.append((char) (RANDOM.nextInt(10) + '0'));
        }
        jsonObject.put("result", "success");
        stringRedisTemplate.opsForValue().set(state.toString(), "true", Duration.ofMinutes(10L));
        String applyCodeUrl = AUTH_URI
                + "?response_type=" + "code"
                + "&client_id=" + appId
                + "&redirect_uri=" + encodeUrl
                + "&state=" + state;
        jsonObject.put("apply_code_url", applyCodeUrl);
        return jsonObject;
    }

    @Override
    public JSONObject receiveCode(String state, String code) {
        JSONObject jsonObject = new JSONObject() {{
            put("result", "failed");
        }};
        if (state == null || code == null) return jsonObject;
        if (stringRedisTemplate.opsForValue().get(state) == null) return jsonObject;

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("client_id", appId));
        params.add(new BasicNameValuePair("client_secret", appKey));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        params.add(new BasicNameValuePair("fmt", "json"));
        String result = HttpClientUtil.get(ACCESS_TOKEN_URL, params);
        if (!StringUtils.hasText(result)) return jsonObject;
        JSONObject parse = JSONObject.parseObject(result);
        String accessToken = parse.getString("access_token");
        if (!StringUtils.hasText(accessToken)) return jsonObject;

        List<NameValuePair> paramsOfMe = new ArrayList<>();
        paramsOfMe.add(new BasicNameValuePair("access_token", accessToken));
        paramsOfMe.add(new BasicNameValuePair("fmt", "json"));
        String resultOfMe = HttpClientUtil.get(OPEN_ID_URL, paramsOfMe);
        if (!StringUtils.hasText(resultOfMe)) return jsonObject;
        JSONObject parseObject = JSONObject.parseObject(resultOfMe);
        String openid = parseObject.getString("openid");
        if (!StringUtils.hasText(openid)) return jsonObject;
        QueryWrapper<User> query =
                new QueryWrapper<>();
        query.eq("open_id", openid);
        User user = userMapper.selectOne(query);
        if (user != null) {
            String jwt = JwtUtil.createJWT(user.getId().toString());
            jsonObject.put("result", "success");
            jsonObject.put("jwt_token", jwt);
            return jsonObject;
        }

        List<NameValuePair> paramOfGetUserInfo = new ArrayList<>();
        paramOfGetUserInfo.add(new BasicNameValuePair("access_token", accessToken));
        paramOfGetUserInfo.add(new BasicNameValuePair("oauth_consumer_key", appId));
        paramOfGetUserInfo.add(new BasicNameValuePair("openid", openid));

        String resultOfUserInfo = HttpClientUtil.get(GET_INFO_URL, paramOfGetUserInfo);
        if (!StringUtils.hasText(resultOfUserInfo)) return jsonObject;
        JSONObject parseObject1 = JSONObject.parseObject(resultOfUserInfo);
        String username = parseObject1.getString("nickname");
        String page = parseObject1.getString("figureurl_1");
        System.out.println("username: " + username);
        if (!StringUtils.hasText(username) || !StringUtils.hasText(page)) return jsonObject;
        for (int i = 0; i < 100; i++) {
            QueryWrapper<User> query1 =
                    new QueryWrapper<>();
            query1.eq("username", username);
            List<User> users = userMapper.selectList(query1);
            if (users.isEmpty()) break;
            if (i == 99) return jsonObject;
            username += RANDOM.nextInt(10);
        }
        User newUser = new User(null, username, null, 1500, page, openid);
        int insert = userMapper.insert(newUser);
        if (insert != 1) return jsonObject;
        String jwt = JwtUtil.createJWT(newUser.getId().toString());
        jsonObject.put("result", "success");
        jsonObject.put("jwt_token", jwt);
        return jsonObject;
    }
}
