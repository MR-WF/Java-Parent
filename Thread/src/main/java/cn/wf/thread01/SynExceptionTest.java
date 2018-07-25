package cn.wf.thread01;

import java.util.concurrent.TimeUnit;

/**
 * @description: 程序执行过程中,如果出现异常，默认情况下已获得的锁会被释放
 * 所以，在并发处理的过程中，有异常要多加小心，不然可能会发生不一致的情况。
 * 比如，在一个web app处理过程中，多个servlet线程共同访问同一个资源，这时如果不处理异常
 * 在第一个线程中抛出异常，其他线程就会进入同步代码区，有可能会访问到异常产生时的数据。
 * @author: it.wf
 * @create: 2018-07-25 19:30
 **/
public class SynExceptionTest {

    //volatile 保证线程之间的数据的可见性
    /**
     *volatile 可以保证线程可见性且提供了一定的有序性，但是无法保证原子性。在 JVM 底层 volatile 是采用“内存屏障”来实现的
     * volatile的内存语义是：
     *     当写一个 volatile 变量时，JMM 会把该线程对应的本地内存中的共享变量值立即刷新到主内存中。
     *     当读一个 volatile 变量时，JMM 会把该线程对应的本地内存设置为无效，直接从主内存中读取共享变量
     *     volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
     */
    volatile boolean flag =true;

    int num =0;
    private synchronized void t1(){
        System.out.println(Thread.currentThread().getName() + "**t1**start");

        while (flag){
            num++;
            System.out.println(Thread.currentThread().getName() + " num->" + num);
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (num == 4){
                //此处抛出异常，线程已获得的锁将被释放，将重新与其他等待的线程竞争去获得该资源，要想不被释放，可以在这里进行catch，然后让循环继续
                //try {
                int i = num / 0;
                //}catch (Exception e){
                 //   break ;
                //    e.printStackTrace();
                //}
            }
            if (num == 1000){
                flag = false;
            }
        }
        System.out.println(Thread.currentThread().getName() + "**t1**end");
    }

    public static void main(String[] args) {
        SynExceptionTest s = new SynExceptionTest();
        new Thread(s::t1).start();
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(s::t1,"线程2").start();


    }

}
