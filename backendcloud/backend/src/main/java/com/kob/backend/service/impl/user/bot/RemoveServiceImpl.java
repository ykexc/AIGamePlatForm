package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.RemoveService;
import com.kob.backend.utils.StaticNamePool;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mqz
 */
@Service
public class RemoveServiceImpl implements RemoveService {

    @Resource
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authenticationToken.getPrincipal();
        Integer botId = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(botId);
        Map<String, String> map = new HashMap<>();
        if (bot == null) {
            map.put(StaticNamePool.ER_MSG, "Bot不存在或已被删除");
            return map;
        }
        if (!bot.getUserId().equals(principal.getUser().getId())) {
            map.put(StaticNamePool.ER_MSG, "没有权限删除该bot");
            return map;
        }
        botMapper.deleteById(botId);
        map.put(StaticNamePool.ER_MSG, "success");
        return map;
    }
}
