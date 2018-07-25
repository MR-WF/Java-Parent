package cn.wf.thread01.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 * 最简单的实现
 * @author: it.wf
 * @create: 2018-07-25 20:04
 **/
public class ContainerTest1 {

    //添加volatile，使得list能够在线程见可见，读写更新时通知其他线程，否则会出现不同步现象
    volatile List lists = new ArrayList();
    public void add(Object o){
        System.out.println(Thread.currentThread().getName()+"-向list中添加了一个元素->"+o);
        lists.add(o);
    }
    public int getSize(){
        return lists.size();
    }

    public static void main(String[] args) {
        ContainerTest1 c = new ContainerTest1();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"---start");
            for (int i = 0; i < 10; i++) {
                c.add(i);
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"---end");
        },"线程1").start();

        new  Thread(()->{
            System.out.println(Thread.currentThread().getName()+"---start");
            //这里用while而不是直接用if
            while (true){
                if (c.getSize() == 5){
                    break;
                }
            }
            System.out.println(Thread.currentThread().getName()+"---end");
        },"线程2").start();
    }
}
