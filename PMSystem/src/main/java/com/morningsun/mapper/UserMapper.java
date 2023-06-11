package com.morningsun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.morningsun.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author morningsun
 * @since 2022-03-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
