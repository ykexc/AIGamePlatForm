package com.kob.backend.filter;

import com.kob.backend.utils.LimitUtil;
import com.kob.common.constant.Constant;
import com.kob.common.resp.RestBean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mqz
 */
@Component
@RequiredArgsConstructor
@Order(111)
public class FlowLimitFilter extends HttpFilter {


    private final StringRedisTemplate redisTemplate;

    private final LimitUtil limitUtil;


    @Value("${limit.base}")
    private int limit;


    @Value("${limit.freq}")
    private int freq;

    @Value("${limit.block}")
    private int block;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {
        String ip = request.getRemoteAddr();

        if (!tryCount(ip)) {
            writeBlockMessage(response);
        } else chain.doFilter(request, response);
    }

    private boolean tryCount(String ip) {
        String counterKey = Constant.Redis.FLOW_LIMIT_COUNTER + ip;
        String blockKey = Constant.Redis.FLOW_LIMIT_BLOCK + ip;
        synchronized (ip.intern()) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blockKey)))
                return false;
            return limitUtil.limitPeriodCheck(counterKey, blockKey, block, freq, limit);
        }
    }


    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(RestBean.forbidden("操作频繁, 请稍后再试").asJsonString());
        writer.close();
    }

}
