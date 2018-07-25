package cn.wf.thread01.example;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description:实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 * ----------------------------------------------------------------------------------------------
 * 方式一：
 * 最简单的实现：t2线程的通过while(true)死循环很浪费cpu
 *
 *-----------------------------------------------------------------------------------------------
 * 方式二：可以用线程本身的wait和notify
 * *****************************************************************
 * 注意:
 * wait会释放锁后等待，而notify不会释放锁
 *需要注意的是，运用这种方法，必须要保证t2先执行，也就是首先让t2监听才可以
 * *****************************************************************
 * 并且:
 * t1在notify()之后，t1必须wait()释放锁，t2退出后，也必须notify()通知t1继续执行
 * -----------------------------------------------------------------------------------------------
 * 方式三：
 * 使用Latch（门闩）替代wait notify来进行通知
 * 好处是通信方式简单，同时也可以指定等待时间
 * 使用await和countdown方法替代wait和notify
 * CountDownLatch不涉及锁定，当count的值为零时,门栓开启当前线程继续运行
 * 当不涉及同步，只是涉及线程通信的时候，用synchronized + wait/notify就显得太重了
 * 这时应该考虑countdownlatch/cyclicbarrier/semaphore
 *
 * @author: it.wf
 * @create: 2018-07-25 21:02
 **/
public class ContainerTest3 {

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
        long start = System.currentTimeMillis();
        ContainerTest3 c = new ContainerTest3();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"---start");
            if (c.getSize() != 5){
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"---end");
        },"线程1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new  Thread(()->{
            System.out.println(Thread.currentThread().getName()+"---start");
            for (int i = 0; i < 10; i++) {
                c.add(i);
                if (c.getSize()==5){
                    //// latch-1变为0，打开门闩，让线程2得以执行
                    latch.countDown();
                }
            }
            System.out.println(Thread.currentThread().getName()+"---end");
        },"线程2").start();

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }



}
