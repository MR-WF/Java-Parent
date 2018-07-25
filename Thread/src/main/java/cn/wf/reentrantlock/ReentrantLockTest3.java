package cn.wf.reentrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantlock可用于替代synchronized,达到相同的同步效果
 * 使用reentrantlock可以完成同样的功能
 * *********************************************************
 * ReentrantLockh额Synchronized的区别：
 * 区别一：
 *       需要注意的是，必须要必须要必须要手动释放锁（重要的事情说三遍）
 *       使用syn锁定的话如果遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，因此经常在finally中进行锁的释放
 * ***************************************************************************
 * 区别二:
 * 使用reentrantlock可以进行“尝试锁定”tryLock，这样无法锁定，或者在指定时间内无法锁定，线程可以决定是否继续等待
 * **************************************************************************
 * 区别4
 * 使用ReentrantLock还可以调用lockInterruptibly方法，可以对线程interrupt方法做出响应---打断
 * 在一个线程等待锁的过程中，可以被打断
 * *************************************************************************
 * 区别4
 * ReentrantLock还可以指定为公平锁
 * Created by Mr_WF on 2018/7/25.
 */
public class ReentrantLockTest3 {

    /**
     * 用ReentrantLock默认不公平锁，由cpu控制分配，性能较好
     */
    //Lock lock = new ReentrantLock();
            //new ReentrantLock(true)公平锁，等待时间越长越先获得竞争资源
    Lock lock = new ReentrantLock(true);

    void t1(){
        for (int i = 0; i < 100; i++) {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+"获得锁");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockTest1 t = new ReentrantLockTest1();
        new Thread(t::t1,"线程1").start();
        new Thread(t::t1,"线程2").start();


    }

}
