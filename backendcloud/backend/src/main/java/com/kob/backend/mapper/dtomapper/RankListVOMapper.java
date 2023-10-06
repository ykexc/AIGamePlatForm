package com.kob.backend.mapper.dtomapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.pojo.vo.RankListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author mqz
 */
@Mapper
public interface RankListVOMapper extends BaseMapper<RankListVO> {



    @Select("SELECT u.username, u.photo, u.rating, IFNULL(MAX(b.rating), 0) AS bot_rating\n" +
            "FROM user u\n" +
            "         LEFT JOIN bot b ON u.id = b.user_id\n" +
            "GROUP BY u.username, u.photo, u.rating\n" +
            "ORDER BY rating DESC")
    IPage<RankListVO> selectRankListPage(Page<RankListVO> page);
}
