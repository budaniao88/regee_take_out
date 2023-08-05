package com.itheima.regee_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.regee_take_out.common.R;
import com.itheima.regee_take_out.service.EmployeeService;
import entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmploeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
    *
    * 员工登录
    * @param request
    * @param employee
    * @return
    *
    * */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());

        Employee emp = employeeService.getOne(queryWrapper);
        if (emp == null){
            return R.error("登录失败");
        }

        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        if (emp.getStatus() == 0){
            return R.error("账号已被禁用");
        }

        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 从session中移除登录信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save( HttpServletRequest request,@RequestBody Employee employee){
        log.info("接收到员工信息:{}",employee.toString());
        // 设置初始密码123456 MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

       //employee.setCreateTime(LocalDateTime.now());
       //employee.setUpdateTime(LocalDateTime.now());

       //Long empId = (Long) request.getSession().getAttribute("employee");
       //employee.setCreateUser(empId);
       //employee.setUpdateUser(empId);
        employeeService.save(employee);

        return R.success("添加员工成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page , int pageSize,String name){
        log.info("接收到分页请求,page:{},pageSize:{},name:{}",page,pageSize,name);
        // 构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        // 构造查询构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        // 排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // 执行分页查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /*
    *
    * 根据id修改员工信息
    * */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("当前线程id:{}",id);

        // Long empId = (Long) request.getSession().getAttribute("employee");
       // employee.setUpdateTime(LocalDateTime.now());
       // employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("修改员工信息成功");
    }


    /*
    *
    * 根据id查询员工信息
    * */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息,id:{}",id);
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
    }

}
