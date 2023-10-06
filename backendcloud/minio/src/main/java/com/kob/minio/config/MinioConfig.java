package com.kob.minio.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mqz
 */
@Configuration
public class MinioConfig {

    @Value("${spring.minio.username}")
    private String username;

    @Value("${spring.minio.password}")
    private String password;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(username, password)
                .endpoint("http://127.0.0.1:9000")
                .build();
    }

}