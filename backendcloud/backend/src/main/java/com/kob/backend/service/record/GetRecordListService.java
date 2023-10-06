package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

/**
 * @author mqz
 */
public interface GetRecordListService {

    JSONObject getRecordList(Integer pageId);

}
