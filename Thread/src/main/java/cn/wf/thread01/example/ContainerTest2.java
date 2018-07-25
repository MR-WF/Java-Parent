package cn.wf.thread01.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 * 最简单的实现：t2线程的通过while(true)死循环很浪费cpu，如果不用死循环，可用wait和notify，
 * 注意:
 * *****************************************************************
 * wait会释放锁后等待，而notify不会释放锁
 *需要注意的是，运用这种方法，必须要保证t2先执行，也就是首先让t2监听才可以
 * *****************************************************************
 * 并且:
 * notify之后，t1必须释放锁，t2退出后，也必须notify，通知t1继续执行
 * ----------------------------------------------------------------
 *
 * @author: it.wf
 * @create: 2018-07-25 20:24
 **/
public class ContainerTest2 {

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
        ContainerTest2 c = new ContainerTest2();
        final Object lock = new Object();

        new  Thread(()->{
            System.out.println(Thread.currentThread().getName()+"---start");
            synchronized (lock){
                if (c.getSize()!=5){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName()+"---end");
                //通知线程1 继续执行
                lock.notify();
            }
        },"线程2").start();
        //保障线程2先启动
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"---start");
            synchronized (lock){
                for (int i = 0; i < 10; i++) {
                    c.add(i);
                    if (c.getSize()==5){
                        lock.notify();
                        //释放锁，让线程2得以执行
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(Thread.currentThread().getName()+"---end");
        },"线程1").start();


    }
}
