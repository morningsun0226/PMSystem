package com.morningsun.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private static final long serialVersionUID = 5L;
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
}
