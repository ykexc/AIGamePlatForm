package com.kob.matchingsystem.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean("matchQueue")
    public Queue matchQueue() {
        return QueueBuilder.durable("match").build();
    }

}
