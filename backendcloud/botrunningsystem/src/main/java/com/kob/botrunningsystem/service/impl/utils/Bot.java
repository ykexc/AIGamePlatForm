package com.kob.botrunningsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mqz
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bot {

    private Integer userId;
    private String botCode;
    private String input;
    private String language;
    private String game;
    private String container;
    private String status;
}
