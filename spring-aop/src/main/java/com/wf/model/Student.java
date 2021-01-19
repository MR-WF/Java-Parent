package com.wf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description:
 * @author: it.wf
 * @create: 2019-12-26 19:12
 **/
@Setter
@Getter
@ToString
public class Student {
    private Long id;
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
