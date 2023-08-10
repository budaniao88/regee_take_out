package com.itheima.regee_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.regee_take_out.dto.SetmealDto;
import entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    /*
    * 删除套餐，同时删除套餐和菜品的关系
    * */
    public void removeWithDish(List<Long> ids);
}
