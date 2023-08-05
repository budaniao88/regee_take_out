package com.itheima.regee_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import entity.Category;

public interface CategoryService extends IService<Category>{
    public  void remove(Long id);

    ;
}
