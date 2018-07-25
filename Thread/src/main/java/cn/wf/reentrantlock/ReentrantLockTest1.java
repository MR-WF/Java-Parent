package cn.wf.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantlock可用于替代synchronized,达到相同的同步效果
 * 使用reentrantlock可以完成同样的功能
 * ****************************************************
 * 需要注意的是，必须要必须要必须要手动释放锁（重要的事情说三遍）
 * 使用syn锁定的话如果遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，因此经常在finally中进行锁的释放
 * Created by Mr_WF on 2018/7/25.
 */
public class ReentrantLockTest1 {

    synchronized void t1(){
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("t1****start");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用ReentrantLock实现上面的t1
     */
    Lock lock = new ReentrantLock();
    void tt1(){
        lock.lock();
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    synchronized void t2() {
        System.out.println("t2****start");
    }

    /**
     * 用ReentrantLock实现上面的t2
     */
    void tt2() {
        lock.lock();
        System.out.println("t2****start");
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLockTest1 rt = new ReentrantLockTest1();
        new Thread(rt::tt1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rt::tt2).start();

    }
}
