package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mqz
 */
@RestController
public class UpdateUserController {

    @Resource
    InfoService infoService;

    @PostMapping("/api/user/update/photo")
    public Map<String, String> updatePhoto(@RequestAttribute Integer userId, String url) {
        return infoService.updatePhoto(userId, url);
    }


}
