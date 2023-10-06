package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
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
public class RemoveController {
    private final RemoveService removeService;

    @PostMapping("/api/user/bot/remove/")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return removeService.remove(data);
    }

}
