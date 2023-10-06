package com.kob.backend.consumer.gobang.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author mqz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GobangPlayer {

    private Integer id;

    private Integer botId;

    private LinkedHashSet<String> points;


    private String container;

    private String language;

    private String botCode;



}
