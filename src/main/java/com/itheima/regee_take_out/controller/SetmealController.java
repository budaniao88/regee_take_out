package com.itheima.regee_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.regee_take_out.common.R;
import com.itheima.regee_take_out.dto.SetmealDto;
import com.itheima.regee_take_out.service.CategoryService;
import com.itheima.regee_take_out.service.SetmealDishService;
import com.itheima.regee_take_out.service.SetmealService;
import entity.Category;
import entity.Setmeal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
 * 套餐管理
 *
 * */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;
    /*
     * 保存新增套餐
     * */

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /*
     * 套餐分页查询
     * */
    @GetMapping("/page")
    private R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据name模糊查询
        queryWrapper.like(name != null, Setmeal::getName, name);
        // 添加排序条件，根据updateTime降序排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 调用service方法进行查询]
        setmealService.page(pageInfo, queryWrapper);
        // 将查询结果封装到dtoPage中
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            // 根据套餐id查询套餐下的菜品
            // 将菜品id集合封装到dto中
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(pageInfo);
    }

    /*
     * 根据id删除套餐信息和菜品信息
     * */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除套餐id：{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除套餐成功");
    }
}