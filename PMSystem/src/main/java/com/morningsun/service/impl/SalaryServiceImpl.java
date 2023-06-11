package com.morningsun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morningsun.entity.Salary;
import com.morningsun.mapper.SalaryMapper;
import com.morningsun.service.ISalaryService;
import org.springframework.stereotype.Service;

@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

}
