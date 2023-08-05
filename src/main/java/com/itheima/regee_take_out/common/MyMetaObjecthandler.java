package com.itheima.regee_take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {

    /*
    * 插入操作自动填充
    * */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入数据填充");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /*
    * 更新操作自动填充
    * */

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新数据填充");
        log.info(metaObject.toString());
        long id = Thread.currentThread().getId();
        log.info("当前线程id:{}",id);

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
