package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.mapper.dtomapper.RankListVOMapper;
import com.kob.backend.pojo.vo.RankListVO;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author mqz
 */
@Service
public class GetRankLIstServiceImpl implements GetRankListService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RankListVOMapper baseMapper;

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
}
