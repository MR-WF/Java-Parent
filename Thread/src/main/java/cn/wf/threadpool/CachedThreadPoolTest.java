package cn.wf.threadpool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  *************************************************************
 * 第一种   线程池FixedThreadPool   newFixedThreadPool创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 * FixedThreadPool 是通过 java.util.concurrent.Executors 创建的 ThreadPoolExecutor 实例。这个实例会复用 固定数量的线程 处理一个 共享的无边界队列 。
 * 任何时间点，最多有 n 个线程会处于活动状态执行任务。如果当所有线程都是活动时，有多的任务被提交过来，那么它会一致在队列中等待直到有线程可用。
 * 如果任何线程在执行过程中因为错误而中止，新的线程会替代它的位置来执行后续的任务。
 * 所有线程都会一致存于线程池中，直到显式的执行 ExecutorService.shutdown() 关闭
 * ********************************************** ******************************************************************************************************
 * 第三种  线程池CachedThreadPool可缓存线程池 	可增长，最大值 Integer.MAX_VALUE
 * CachedThreadPool 是通过 java.util.concurrent.Executors 创建的 ThreadPoolExecutor 实例。这个实例会根据需要，在线程可用时，重用之前构造好的池中线程。
 * 这个线程池在执行 大量短生命周期的异步任务时（many short-lived asynchronous task），可以显著提高程序性能。调用 execute 时，可以重用之前已构造的可用线程，
 * 如果不存在可用线程，那么会重新创建一个新的线程并将其加入到线程池中。
 * 如果线程超过 60 秒还未被使用，就会被中止并从缓存中移除。
 * 因此，线程池在长时间空闲后不会消耗任何资源。
 * **********************************************************************************************************************************************************
 * 第三种  线程池SingleThreadPool单线程化的线程池
 * SingleThreadPool 是通过 java.util.concurrent.Executors 创建的 ThreadPoolExecutor 实例。这个实例只会使用单个工作线程来执行一个无边界的队列。
 *  创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
 * （注意，如果单个线程在执行过程中因为某些错误中止，新的线程会替代它执行后续线程）。它可以保证认为是按顺序执行的，任何时候都不会有多于一个的任务处于活动状态。
 * 和 newFixedThreadPool(1) 的区别在于，如果线程遇到错误中止，它是无法使用替代线程的。
 * ************************************************************************************************************************************************************
 * 第四种 newScheduledThreadPool 定时线程池
 * 创建一个定长线程池，支持定时及周期性任务执行，ScheduledExecutorService比Timer更安全，功能更强大
 * Created by Mr_WF on 2018/7/28.
 */
public class CachedThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        System.out.println(es);
        for (int i = 0; i < 2; i++) {
            es.execute(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }

        System.out.println(es);

        try {
            //验证默认60回收机制
            TimeUnit.SECONDS.sleep(80);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(es);
    }


    /**
     *调度线程池
     */
    private void singleThreadPool(){
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 6; i++) {
            final int a = i;
            es.execute(()->{
                System.out.println(Thread.currentThread().getName()+"---"+a);
            });
        }
    }

    /**
     * 调度线程池
     */
    private void scheduledThreadPool(){
        ScheduledExecutorService se = Executors.newScheduledThreadPool(4);
        //按一定的周期执行任务，第一个参数runnable是一个task，第二个参数initialDelay第一个任务延迟多长时间执行，第三个参数period 执行的周期，第三个参数时间的单位
        //执行任务小于延迟时间时，第一个任务执行之后，延迟指定时间，然后开始执行第二个任务
        se.scheduleAtFixedRate(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },0,500,TimeUnit.MILLISECONDS);

        //带延迟的，第三个参数delay 延迟的时间
        //当执行任务大于延迟时间时，第一个任务执行之后，延迟指定时间，然后开始执行第二个任务。
        se.scheduleWithFixedDelay(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },0,100,TimeUnit.MILLISECONDS);
    }
}
