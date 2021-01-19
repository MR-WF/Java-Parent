package com.wf.service;

import com.wf.model.Student;

import java.util.List;

/**
 * @description:
 * @author: it.wf
 * @create: 2019-12-26 19:15
 **/
public interface StudentService {
    void insert(Student student);
    List<Student> get(Student student);
}
