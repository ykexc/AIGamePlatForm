package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import com.kob.backend.utils.StaticNamePool;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mqz
 */
@Service
public class AddServiceImpl implements AddService {

    @Resource
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = principal.getUser();
        String title = data.get("title");
        String description = data.get(("description"));
        String content = data.get("content");
        String type = data.get("type");
        String game = data.get("game");
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.hasText(title) || title.isEmpty()) {
            map.put(StaticNamePool.ER_MSG, "标题不能为空");
        }
        if (title.length() > 100) {
            map.put("error_message", "标题长度不能超过100");
            return map;
        }
        if (!StringUtils.hasText(type)) {
            map.put(StaticNamePool.ER_MSG, "语言不能为空");
            return map;
        }
        if (!StringUtils.hasText(description)) {
            description = "这个用户很懒，什么也没有留下~";
        }
        if (description.length() > 300) {
            map.put(StaticNamePool.ER_MSG, "Bot描述的长度不能大于300");
            return map;
        }
        if (!StringUtils.hasText(content)) {
            map.put(StaticNamePool.ER_MSG, "bot代码不能为空");
            return map;
        }
        QueryWrapper<Bot> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(wrapper) >= 10) {
            map.put(StaticNamePool.ER_MSG, "每个用户最多只能有10个bot");
        }


        Date date = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, type, 1000, date, date, game);
        botMapper.insert(bot);
        map.put(StaticNamePool.ER_MSG, "success");
        return map;
    }
}
