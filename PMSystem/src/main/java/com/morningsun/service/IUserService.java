package com.morningsun.service;

import com.morningsun.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.morningsun.entity.User;
import com.morningsun.entity.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author morningsun
 * @since 2022-03-12
 */
public interface IUserService extends IService<User> {

    boolean login(UserDTO userDTO);

    boolean register(UserDTO userDTO);
}
