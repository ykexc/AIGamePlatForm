package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author mqz
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Update("update user set photo = #{url} where id = #{userId}")
    int updatePhotoById(Integer userId, String url);

}
