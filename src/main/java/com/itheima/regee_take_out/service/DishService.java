package com.itheima.regee_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.regee_take_out.dto.DishDto;
import entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品，同时插入菜品口味，操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishdto);

    public DishDto getByIdWithFlavor(Long id);

    // 更新菜品，同时更新菜品口味，操作两张表：dish、dish_flavor
    public void updateWithFlavor(DishDto dishDto);
}
