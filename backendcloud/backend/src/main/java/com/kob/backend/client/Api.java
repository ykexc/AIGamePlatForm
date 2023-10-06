package com.kob.backend.client;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.kob.common.constant.Constant;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mqz
 */
@Component
public interface Api {

    String COMPILE = Constant.COMPILE;

    String RUN = Constant.RUN;

    @Post(
            value = COMPILE,
            headers = {
                    "X-ECC-Current-Tenant: 10000",
                    "Accept-Language: zh-CHS"
            }
    )
    Map<String, String> compile(@JSONBody Map data);

    @Post(
            value = RUN,
            headers = {
                    "X-ECC-Current-Tenant: 10000",
                    "Accept-Language: zh-CHS"
            }
    )
    Map<String, String> run(@JSONBody Map<String, String> data);

}
