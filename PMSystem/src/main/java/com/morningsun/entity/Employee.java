package com.morningsun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("employee")
public class Employee {

    private static final long serialVersionUID = 2L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String department;

    private String name;

    private Integer attdays;

    private Integer realattdays;

    private double basesalary;

    private double allowance;

    private double meal;

    private double overtime;

    private double fullmoney;

    private double security;

    private double accumulation;

    private double taxation;

    private double otherdeductions;

    private double actualhair;

}
