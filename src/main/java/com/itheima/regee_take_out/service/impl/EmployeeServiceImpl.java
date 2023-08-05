package com.itheima.regee_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.regee_take_out.mapper.EmployeMapper;
import entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeMapper, Employee> implements com.itheima.regee_take_out.service.EmployeeService {

}
