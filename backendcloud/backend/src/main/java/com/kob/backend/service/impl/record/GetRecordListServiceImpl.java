package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mqz
 */
@Service
public class GetRecordListServiceImpl implements GetRecordListService {

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getRecordList(Integer pageId) {
        IPage<Record> iPage = new Page<>(pageId, 10);
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Record> records = recordMapper.selectPage(iPage, wrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new ArrayList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            String userAPhoto = userA.getPhoto();
            String userBPhoto = userB.getPhoto();
            JSONObject item = new JSONObject();
            String result = "平局";
            if ("A".equals(record.getLoser())) {
                result = "B胜";
            } else if ("B".equals(record.getLoser())) {
                result = "A胜";
            }
            item.put("result", result);
            item.put("a_photo", userAPhoto);
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userBPhoto);
            item.put("b_username", userB.getUsername());
            item.put("record", record);
            items.add(item);
        }
        resp.put("records", items);
        resp.put("records_count", recordMapper.selectCount(null));
        return resp;
    }
}
