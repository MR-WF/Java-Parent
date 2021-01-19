package com.wf.service.impl;

import com.wf.model.Student;
import com.wf.security.CheckPoint;
import com.wf.service.AuthService;
import com.wf.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: it.wf
 * @create: 2019-12-26 19:16
 **/
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private AuthService authService;
    @Override
    @CheckPoint
    public void insert(Student student) {
        //authService.checkAuth();
        System.out.println("新增对象："+student.toString());
    }

    @Override
    @CheckPoint
    public List<Student> get(Student student) {
        //authService.checkAuth();
        System.out.println("查询对象:"+student.getId());
        return null;
    }
}
