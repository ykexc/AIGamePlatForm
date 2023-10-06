package com.kob.botrunningsystem.client;

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


    String RUN = Constant.RUN;

    @Post(
            value = RUN,
            headers = {
                    "X-ECC-Current-Tenant: 10000",
                    "Accept-Language: zh-CHS"
            }
    )
    Map<String, String> run(@JSONBody Map data);

}
