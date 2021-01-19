package com.wf.security;

import com.wf.service.AuthService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * SpringAOP
 * 一，注解的方式
 * Aspect 注解
 * @Aspect ("execution(* com.savage.aop.MessageSender.*(..))") 说明类  切面的配置类
 * @Pointcut //Pointcut表示式
 * 1）execution(* *(..))
 * //表示匹配所有方法
 * 2）execution(public * com. savage.service.UserService.*(..))
 * //表示匹配com.savage.server.UserService中所有的公有方法
 * 3）execution(* com.savage.server..*.*(..))
 * //表示匹配com.savage.server包及其子包下的所有方法
 * @Advice  增强（通知）  前置，后置，环绕，异常
 *
 *
 *
 */


/**
 * @description:
 * @author: it.wf
 * @create: 2019-12-26 20:05
 **/
@Aspect
@Component
public class SecurityAspect {

    @Autowired
    AuthService authService;

    @Pointcut("@annotation(CheckPoint)")
    public void checkPoint(){

    }

    @Before("checkPoint()")
    public void check(){
        authService.checkAuth();
    }
}
