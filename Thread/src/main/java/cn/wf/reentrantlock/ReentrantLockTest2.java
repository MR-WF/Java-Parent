package cn.wf.reentrantlock;

import java.util.concurrent.TimeUnit;
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
 * Created by Mr_WF on 2018/7/25.
 */
public class ReentrantLockTest2 {

    /**
     * 用ReentrantLock
     */
    Lock lock = new ReentrantLock();

    /**
     * 用ReentrantLock的tryLock
     *      使用tryLock进行尝试锁定，不管锁定与否，方法都将继续执行
     *      可以根据tryLock的返回值来判定是否锁定
     *      也可以指定tryLock的时间，由于tryLock(time)抛出异常，所以要注意unclock的处理，必须放到finally中
     */
    void tt2() {
        boolean locked = lock.tryLock();
        if (locked){
            System.out.println("t2****start");
            lock.unlock();
        }
    }

    void tt3() {
        boolean locked = false;
        try {
            //5分钟内尝试锁定资源，否则结束
            locked = lock.tryLock(5,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (locked) lock.unlock();
        }
    }


    void tt4() {
        Thread t = new Thread(()->{
            boolean locked = false;

            try {
                //lock.lock()线程无法被打断
                //locked = lock.lock();
                //可以对interrupt()方法做出响应
                lock.lockInterruptibly();
                System.out.println("线程开启*******start");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
               /* if (locked){
                    lock.unlock();
                }*/
            }
        });
        t.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打断线程2的等待,如果上面用lock.lock()。则线程无法被打断
        t.interrupt();

    }

    public static void main(String[] args) {
        ReentrantLockTest2 rt = new ReentrantLockTest2();
        //new Thread(rt::tt2).start();
        new Thread(rt::tt3).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rt::tt2).start();

    }
}
