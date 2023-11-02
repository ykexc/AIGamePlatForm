package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.mapper.dtomapper.RankListVOMapper;
import com.kob.backend.pojo.vo.DayRankVo;
import com.kob.backend.pojo.vo.RankListVO;
import com.kob.backend.service.ranklist.GetRankListService;
import com.kob.common.resp.RestBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author mqz
 */
@Service
public class GetRankLIstServiceImpl implements GetRankListService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RankListVOMapper baseMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public JSONObject getRankList(Integer pageId) {
        Page<RankListVO> page = new Page<>(pageId, 3);
        IPage<RankListVO> rankListDtoIPage = baseMapper.selectRankListPage(page);
        List<RankListVO> users = rankListDtoIPage.getRecords();
        JSONObject resp = new JSONObject();
        resp.put("users", users);
        resp.put("users_count", userMapper.selectCount(null));
        //  System.out.println(users);
        return resp;
    }

    @Override
    public RestBean<List<DayRankVo>> getDayRankList() {
        Set<ZSetOperations.TypedTuple<Object>> rank =
                redisTemplate.opsForZSet().reverseRangeWithScores("rank", 0, 9);
        List<DayRankVo> res = new ArrayList<>(10);
        assert rank != null;
        rank.forEach(e -> {
            DayRankVo value = (DayRankVo) e.getValue();
            assert value != null;
            assert e.getScore() != null;
            double score = e.getScore();
            value.setScore((int) score);
            res.add(value);
        });
        return RestBean.success(res);
    }
}
