package com.kob.minio.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(-1)
public class CorsFilter extends HttpFilter {


    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain
    ) throws ServletException, IOException {
        addCorsHeader(request, response);
        chain.doFilter(request, response);
    }

    private void addCorsHeader(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", resolveOrigin(request));
        response.addHeader("Access-Control-Allow-Methods", resolveMethod());
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }


    private String resolveMethod() {
        return "GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE, PATCH";
    }

    private String resolveOrigin(HttpServletRequest request) {
        return request.getHeader("Origin");
    }
}