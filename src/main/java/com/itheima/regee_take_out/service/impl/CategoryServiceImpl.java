package com.itheima.regee_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.regee_take_out.common.CustomException;
import com.itheima.regee_take_out.mapper.CategoryMapper;
import com.itheima.regee_take_out.service.CategoryService;
import com.itheima.regee_take_out.service.DishService;
import com.itheima.regee_take_out.service.SetmealService;
import entity.Category;
import entity.Dish;
import entity.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /*
    * 根据ID删除分类，删除前需要判断该分类下是否有菜品，如果有菜品则不能删除
    * */
    @Override
    public  void remove(Long id){
        //根据分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询是否关联菜品
        if (count1>0){
            throw new CustomException("该分类下有菜品，不能删除");
        }
        //查询是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            throw new CustomException("该分类下有套餐，不能删除");
        }
        //删除分类
        super.removeById(id);
    }
}
