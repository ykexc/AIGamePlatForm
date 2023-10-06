package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author mqz
 */
@RestController
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @PostMapping(value = "/api/user/account/register")
    public Map<String, String> register(@RequestParam Map<String, String> userMap) {
        String username = userMap.get("username");
        String password = userMap.get("password");
        String confirmedPassword = userMap.get("confirmedPassword");
        return registerService.register(username, password, confirmedPassword);
    }

}
