package com.kob.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mqz
 */
@RestController
@RequiredArgsConstructor
public class IndexController {


    @GetMapping("/hello")
    public String index() {
        return "OK";
    }


}
