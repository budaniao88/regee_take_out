package com.itheima.regee_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.regee_take_out.dto.DishDto;
import com.itheima.regee_take_out.mapper.DishMapper;
import com.itheima.regee_take_out.service.DishFlavorService;
import com.itheima.regee_take_out.service.DishService;
import entity.Dish;
import entity.DishFlavor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /*
    *
    * 新增菜品，同时保存口味数据
    * */
    @Transactional
    public void saveWithFlavor(DishDto dishdto) {
        // 1.保存菜品
        this.save(dishdto);

        Long dishId = dishdto.getId(); // 菜品id

        // 菜品口味
        List<DishFlavor> flavors = dishdto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 2.保存菜品口味
        dishFlavorService.saveBatch(flavors);
    }


    /*
     * 根据id查询菜品信息和口味信息
     * */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);


        // 查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 1.更新菜品信息
        this.updateById(dishDto);

        // 2.更新菜品口味信息
        // 2.1 先删除原来的口味信息
        //v
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // 2.2 保存新的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
