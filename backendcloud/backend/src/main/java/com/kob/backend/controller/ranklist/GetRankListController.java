package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.pojo.vo.DayRankVo;
import com.kob.backend.service.ranklist.GetRankListService;
import com.kob.common.resp.RestBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author mqz
 */
@RestController
public class GetRankListController {

    @Resource
    private GetRankListService getRankListService;

    @GetMapping("/api/ranklist/getlist/")
    public JSONObject getRankList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return getRankListService.getRankList(page);
    }


    @GetMapping("/api/ranklist/day")
    public RestBean<List<DayRankVo>> getDayRankList() {
        return getRankListService.getDayRankList();
    }



}
