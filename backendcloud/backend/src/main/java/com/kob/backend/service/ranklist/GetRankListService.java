package com.kob.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.pojo.User;

import java.util.List;

/**
 * @author mqz
 */
public interface GetRankListService {

    JSONObject getRankList(Integer pageId);
}
