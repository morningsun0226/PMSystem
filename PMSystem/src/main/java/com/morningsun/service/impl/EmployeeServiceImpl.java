package com.morningsun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morningsun.entity.Employee;
import com.morningsun.mapper.EmployeeMapper;
import com.morningsun.service.IEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
