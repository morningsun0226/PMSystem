package com.morningsun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("department")
public class Department {

    private static final long serialVersionUID = 3L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String dname;

    private String member;

    private String position;

}
