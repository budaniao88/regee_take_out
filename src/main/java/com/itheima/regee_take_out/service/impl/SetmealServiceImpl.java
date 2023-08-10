package com.itheima.regee_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.regee_take_out.common.CustomException;
import com.itheima.regee_take_out.dto.SetmealDto;
import com.itheima.regee_take_out.mapper.SetmealMapper;
import com.itheima.regee_take_out.service.SetmealDishService;
import com.itheima.regee_take_out.service.SetmealService;
import entity.Setmeal;
import entity.SetmealDish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /*
    *
    * 新增套餐。同时需要新增套餐和菜品的关系
    * */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息 , 操作setmeal, insert 操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        // 利用流给每个setmealDish设置setmealId
        setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关系 , 操作setmeal_dish, insert 操作
        setmealDishService.saveBatch(setmealDishes);
    }
    /*
    * 删除套餐。同时需要删除套餐和菜品的关系
    * */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        // 不能删除，抛出异常

        int count = this.count(queryWrapper);
        if (count>0){
            throw new CustomException("套餐售卖中，不能删除");
        }

        //  如果可以删除，删除套餐表数据-setmeal
        this.removeByIds(ids);

        // 删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }
}
