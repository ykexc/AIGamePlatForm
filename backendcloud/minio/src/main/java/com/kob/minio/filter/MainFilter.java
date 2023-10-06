package com.kob.minio.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mqz
 */
@Component
public class MainFilter extends HttpFilter {

    @Override
    protected void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String ip = request.getRemoteAddr();
        //TODO  使用redis进行限流
//        if (!"127.0.0.1".equals(ip)) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String resp = objectMapper.writeValueAsString(new Res("非法ip访问"));
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.setContentType("application/json;charset=utf-8");
//            PrintWriter out = response.getWriter();
//            out.write(resp);
//            return;
//        }
        chain.doFilter(request, response);

    }

    @AllArgsConstructor
    static class Res {
        String message;
    }
}
