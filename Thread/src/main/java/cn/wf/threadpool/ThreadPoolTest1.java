package cn.wf.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Java线程池的
 * 1.根是接口java.util.concurrent.Executor,只有一个方法execute（Runnable command）
 * 2.ExecutorService是Executor的实现服务类，添加了一些实用的方法，泪如submit(Runnable command),submit(Callable command)有返回值，shoutdown().shoutdownNow()等
 * 3.Executors是一个工具类，主要提供了一些线程操作的static方法，比如创建线程池，ExecutorService executorService = Executors.newFixedThreadPool(5);返回一个ExecutorService
 * 4.Future
 *       Future<T> f =executorService.submit(thread)；  executorService.submit（t）会返回一个FutureTask
 *       FutureTask<T> f =new FutureTask(()->{return t;}) 未来的任务
 *5  Callable，对Runnable进行了扩展
 *   对Callable的调用，可以有返回值 interface Callable<V> 返回值V
 *
 *
 *************************************************************
 * 第一种线程池FixedThreadPool   newFixedThreadPool创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 * FixedThreadPool 是通过 java.util.concurrent.Executors 创建的 ThreadPoolExecutor 实例。这个实例会复用 固定数量的线程 处理一个 共享的无边界队列 。
 * 任何时间点，最多有 nThreads 个线程会处于活动状态执行任务。如果当所有线程都是活动时，有多的任务被提交过来，那么它会一致在队列中等待直到有线程可用。
 * 如果任何线程在执行过程中因为错误而中止，新的线程会替代它的位置来执行后续的任务。
 * 所有线程都会一致存于线程池中，直到显式的执行 ExecutorService.shutdown() 关闭
 *
 *
 * Created by Mr_WF on 2018/7/28.
 */
public class ThreadPoolTest1 {

    public static void main(String[] args) {
       /* new MyExecutor().execute(()->
                System.out.println("execute------------")
        );*/

        ExecutorService es = Executors.newFixedThreadPool(5);
        //newFixedThreadPool 8个任务，线程池核心线程为5，则有5个执行，3个在BlockingQueue中等待，当有空闲线程的时候，开始执行队列中的任务，不会创建新的线程
        for (int i = 0; i < 8; i++) {
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
        //关闭线程池服务，服务不会立即关闭，而是等线程池里的线程都执行完毕，才会关闭
        //但是它不再接收新的任务，直到当前所有线程执行完成才会关闭，所有在shutdown()执行之前提交的任务都会被执行。
        es.shutdown();
        //这个动作将跳过所有正在执行的任务和被提交还没有执行的任务。但是它并不对正在执行的任务做任何保证，有可能它们都会停止，也有可能执行完成
        //es.shutdownNow();


        //es.isTerminated() 线程池中线程是否执行完毕
        System.out.println(es.isTerminated());
        //es.isShutdown()服务是否关闭
        System.out.println(es.isShutdown());
        //服务情况
        System.out.println(es);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(es.isTerminated());
        System.out.println(es.isShutdown());
        System.out.println(es);


    }

}

class MyExecutor implements Executor{

    @Override
    public void execute(Runnable command) {
        //可以另起一个线程去run
        new Thread(command).run();
        //也可以直接在主线程去run
        command.run();
    }
}
