package com.itheima.regee_take_out.common;
/*
*
* 基于ThreadLocal的封装工具类
* 保存和获取当前线程中的数据
* */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /*
    * 设置值
    * */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    /*
    * 获取值
    * */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
