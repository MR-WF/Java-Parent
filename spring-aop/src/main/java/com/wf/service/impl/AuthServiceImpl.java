package com.wf.service.impl;

import com.wf.security.CurrentStuHolder;
import com.wf.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: it.wf
 * @create: 2019-12-26 19:49
 **/
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean checkAuth() {
        boolean isAccess = true;
       /* if ("admin".equals(name)){
            System.out.println("验证通过...");
        }else {
            System.out.println("验证失败...");
            throw new RuntimeException("验证失败...");
            //isAccess = false;
        }*/

        if ("admin".equals( CurrentStuHolder.get())){
            System.out.println("验证通过...");
        }else {
            System.out.println("验证失败...");
            throw new RuntimeException("验证失败...");
            //isAccess = false;
        }
        return isAccess;


    }
}
