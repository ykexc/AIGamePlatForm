package com.kob.backend.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author mqz
 */
@Data
public class RankListVO {


    @TableId
    private String username;

    @TableField("rating")
    private Integer rating;

    @TableField("bot_rating")
    private Integer botRating;

    @TableField("photo")
    private String photo;

}
