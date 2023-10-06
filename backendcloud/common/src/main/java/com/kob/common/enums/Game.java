package com.kob.common.enums;

/**
 * @author mqz
 */
public enum Game {


    SNAKE("snake"),

    GOBANG("gobang");

    private final String name;
    Game(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}