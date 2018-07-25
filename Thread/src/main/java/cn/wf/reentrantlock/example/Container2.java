package cn.wf.reentrantlock.example;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 ***********************************************************
 * 方式一:
 *      使用wait和notify/notifyAll来实现，记住while和wait()一起用，而不是if
 ********************************************************************************
 * 方式二:
 * 使用Lock配合Condition(条件)来实现
 * 对比两种方式，Condition的方式可以更加精确的指定哪些线程被唤醒
 * Created by Mr_WF on 2018/7/25.
 */
public class Container2<T> {
    final private LinkedList<T> list = new LinkedList<T>();

    //固定容器
    private final  int MAX = 10;
    //当前资源数
    private int currCount ;

    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    void add(T t){
        //此处用while而不是用if，为了保障每次执行都要先判定当前容器内的元素个数，if可能出现在wait()后唤醒，不再判定出现问题
        //记住while和wait()一起用
        lock.lock();
        while (list.size() == MAX){
            try {
                //生产者等待
                producer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        list.add(t);
        currCount++;
        System.out.println("生产资源:"+t);
        ////通知所有的消费者和生产者，消费者线程进行消费
        //如果用notify(),则可能还只是唤醒生成者，只有一个生产者，达到MAX造成死锁
        //this.notifyAll();
        //生产完成，通知所有的消费者消费
        consumer.signalAll();
    }

    synchronized T get(){
        System.out.println("当前容器数量："+currCount);
        T t ;
        lock.lock();
        while (list.size() == 0){
            try {
                //消费者等待
                consumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        t = list.removeFirst();
        currCount--;
        System.out.println("消费资源:"+t);
        //this.notifyAll();
        //消费完成，通知所有的生产者进行生产资源
        producer.signalAll();
        return  t;
    }

    public static void main(String[] args) {
        Container2<String> c = new Container2<>();
        //启动消费者线程
        for(int i=0; i<10; i++) {
            new Thread(()->{
                for(int j=0; j<5; j++)
                    c.get();
            }, "消费者" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动生产者线程
        for(int i=0; i<2; i++) {
            new Thread(()->{
                for(int j=0; j<25; j++) c.add(j+"");
            }, "生产者" + i).start();
        }
    }

}
