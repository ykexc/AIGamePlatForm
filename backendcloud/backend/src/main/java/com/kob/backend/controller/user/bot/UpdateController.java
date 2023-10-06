package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author mqz
 */
@RestController
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PostMapping("/api/user/bot/update/")
    public Map<String, String> update(@RequestParam Map<String, String> data) {
        return updateService.update(data);
    }

}
