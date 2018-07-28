package cn.wf.threadpool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 第五种线程池  工作窃取线程池  当一个线程的线程等待的队列任务执行完毕，回去其他线程的队列中窃取任务来做
 * 底层是ForkJoinPool（分支合并线程池）
 * Created by Mr_WF on 2018/7/28.
 */
public class WorkStealingPool {
    public static void main(String[] args) {
        //默认池中建立可用线程数个线程
        ExecutorService es = Executors.newWorkStealingPool();
        //可用线程数
        System.out.println(Runtime.getRuntime().availableProcessors());

        es.execute(new MyRun(1000));
        es.execute(new MyRun(2000));
        es.execute(new MyRun(3000));
        es.execute(new MyRun(1000));
        es.execute(new MyRun(2000));

        //daemon线程 由于产生的是精灵线程（守护线程、后台线程），主线程不阻塞的话，看不到输出
        /***
         * 守护线程使用的情况较少，但并非无用，举例来说，JVM的垃圾回收、内存管理等线程都是守护线程。还有就是在做数据库应用时候，使用的数据库连接池，连接池本身也包含着很多后台线程，监控连接个数、超时时间、状态等等。
         * 调用线程对象的方法setDaemon(true)，则可以将其设置为守护线程。守护线程的用途为：
         *      • 守护线程通常用于执行一些后台作业，例如在你的应用程序运行时播放背景音乐，在文字编辑器里做自动语法检查、自动保存等功能。
         *      • Java的垃圾回收也是一个守护线程。守护线的好处就是你不需要关心它的结束问题。例如你在你的应用程序运行的时候希望播放背景音乐，
         *       如果将这个播放背景音乐的线程设定为非守护线程，那么在用户请求退出的时候，不仅要退出主线程，还要通知播放背景音乐的线程退出；如果设定为守护线程则不需要了。
         *
         *
         *JRE判断程序是否执行结束的标准是所有的前台执线程行完毕了，而不管后台线程的状态，因此，在使用后台县城时候一定要注意这个问题
         */
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class MyRun implements Runnable{
        int time;
        MyRun(int t) {
            this.time = t;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"******"+time);
        }
    }

}
