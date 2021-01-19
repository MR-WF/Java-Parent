package com.wf.service.impl;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wf.model.Student;
import com.wf.security.CurrentStuHolder;
import com.wf.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    StudentService studentService;

    @Test(expected = Exception.class)
    public  void checkAuth(){
        CurrentStuHolder.set("admin");
        studentService.insert(new Student("123",1));
    }


    /*@Test
    public  void checkAuthAop(){
        CurrentStuHolder.set("123");
        studentService.get(new Student("123",1));
    }*/

}
