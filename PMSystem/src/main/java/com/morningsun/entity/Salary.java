package com.morningsun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("salary")
public class Salary {

    private static final long serialVersionUID = 4L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String owner;

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

    private LocalDateTime paytime;

}
