package com.kob.common.resp;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mqz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestBean<T> {

    private int code;

    private T data;

    private String message;


    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "OK");
    }


    public static <T> RestBean<T> success() {
        return success(null);
    }

    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    public static <T> RestBean<T> forbidden(String message) {
        return failure(403, message);
    }

    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

}
