package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import com.kob.backend.utils.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mqz
 */
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {


    private final UserMapper userMapper;


    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }

    @Override
    public Map<String, String> updatePhoto(Integer userId, String url) {
        Map<String, String> map = new HashMap<>();
        int update = userMapper.updatePhotoById(userId, url);
        if (update != 1) {
            map.put("error_message", "failure");
        } else {
            map.put("error_message", "success");
        }
        return map;
    }

}
