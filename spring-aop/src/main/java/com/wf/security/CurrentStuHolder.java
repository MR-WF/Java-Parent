package com.wf.security;

/**
 * @description:  模拟缓存当前登陆对象
 * @author: it.wf
 * @create: 2019-12-26 19:45
 **/
public class CurrentStuHolder {
    //存放当前登陆对象
    private static final ThreadLocal<String> stuHolder= new ThreadLocal<>();

    public static String get(){
        return stuHolder.get()==null?"":stuHolder.get();
    }

    public static void set(String value){
        stuHolder.set(value);
    }
}
