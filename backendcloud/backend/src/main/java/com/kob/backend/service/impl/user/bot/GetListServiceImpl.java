package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.GetListService;
import com.kob.common.enums.Game;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mqz
 */
@Service
public class GetListServiceImpl implements GetListService {

    @Resource
    private BotMapper botMapper;

    @Override
    public List<Bot> getSnakeBotList() {
        return getList(Game.SNAKE.getName());
    }


    @Override
    public List<Bot> getGobangBotList() {
        return getList(Game.GOBANG.getName());
    }

    @Override
    public List<Bot> getBotList(Integer userId) {
        return botMapper.selectList(new QueryWrapper<Bot>() {{
            eq("user_id", userId);
        }});
    }

    public List<Bot> getList(String game) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authenticationToken.getPrincipal();
        Integer id = principal.getUser().getId();
        QueryWrapper<Bot> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        wrapper.eq("game", game);
        return botMapper.selectList(wrapper);
    }
}
