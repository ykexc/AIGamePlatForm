package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import com.kob.backend.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mqz
 */
@Service
@Slf4j
public class RegisterServiceImpl extends ServiceImpl<UserMapper, User> implements RegisterService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (password == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }
        if (confirmedPassword == null) {
            map.put("error_message", "请确认密码");
            return map;
        }
        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "用户名不能为空");
            return map;
        }
        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "密码不能大于100");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }
        List<User> users = query().eq("username", username).list();
        if (!users.isEmpty()) {
            map.put("error_message", "用户名已存在");
            return map;
        }
        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/196275_lg_23593e701b.jpeg";
        User newUser = new User(null, username, encodePassword, 1500, photo, null);
        save(newUser);
        log.info("new User id is: " + newUser.getId());
        String jwtToken = JwtUtil.createJWT(newUser.getId().toString());
        map.put("token", jwtToken);
        map.put("error_message", "success");
        return map;
    }
}
