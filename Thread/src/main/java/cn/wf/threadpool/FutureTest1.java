package cn.wf.threadpool;

import java.util.concurrent.*;

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
 * 通过Callable和Future创建线程的具体步骤和具体代码如下：

 • 创建Callable接口的实现类，并实现call()方法，该call()方法将作为线程执行体，并且有返回值。
 • 创建Callable实现类的实例，使用FutureTask类来包装Callable对象，该FutureTask对象封装了该Callable对象的call()方法的返回值。
 • 使用FutureTask对象作为Thread对象的target创建并启动新线程。
 • 调用FutureTask对象的get()方法来获得子线程执行结束后的返回值其中，Callable接口(也只有一个方法)定义如下：
 *
 *
 * 步骤1：创建实现Callable接口的类SomeCallable(略);
 * 步骤2：创建一个类对象：
 * Callable oneCallable = new SomeCallable();
 * 步骤3：由Callable创建一个FutureTask对象：
 * FutureTask oneTask = new FutureTask(oneCallable);
 * 注释： FutureTask是一个包装器，它通过接受Callable来创建，它同时实现了 Future和Runnable接口。
 * 步骤4：由FutureTask创建一个Thread对象：
 * Thread oneThread = new Thread(oneTask);
 * 步骤5：启动线程：
 * oneThread.start();
 *
 *
 * Created by Mr_WF on 2018/7/28.
 */
public class FutureTest1 {

    public static void main(String[] args) {
        FutureTask<Integer> task = new FutureTask<Integer>(()->{
            TimeUnit.MILLISECONDS.sleep(500);
            return 10;
        });//FutureTask里面是一个new Callable () { Integer call();}有返回值
        new Thread(task).start();

        try {
            //task.get()会阻塞，知道callable方法执行完后返回返回值
            System.out.println(task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //同样的功能，使用线程池

        ExecutorService es = Executors.newFixedThreadPool(5);
        Future<Integer> f = es.submit(() -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return 100;
        });
        try {
            //阻塞等待返回结果
            System.out.println(f.get());
            System.out.println(f.isDone());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
