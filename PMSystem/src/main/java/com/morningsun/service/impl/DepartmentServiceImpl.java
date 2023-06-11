package com.morningsun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morningsun.entity.Department;
import com.morningsun.mapper.DepartmentMapper;
import com.morningsun.service.IDepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
