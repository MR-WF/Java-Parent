package cn.wf.thread01;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal线程局部变量
 * ThreadLocal的变量值是属于线程本身的
 * ThreadLocal是使用空间换时间，synchronized是使用时间换空间
 *
 * 比如在hibernate中session就存在与ThreadLocal中，避免synchronized的使用
 * Created by Mr_WF on 2018/7/22.
 */
public class ThreadLocalTest2 {

    static ThreadLocal<Person> tp = new ThreadLocal<>();

    public static void main(String[] args) {

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(tp.get());
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tp.set(new Person("小明"));

        }).start();
    }

    static class Person {
        String name ;

        public Person(String name) {
            this.name = name;
        }
    }

}
