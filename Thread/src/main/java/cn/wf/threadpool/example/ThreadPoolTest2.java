package cn.wf.threadpool.example;

import java.util.ArrayList;
import java.util.List;
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
 *
 *
 * Created by Mr_WF on 2018/7/28.
 */
public class ThreadPoolTest2 {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        List<Integer> list = MyTask.getPrime(1,200000);
        System.out.println("得到素数个数:"+list.size());
        long end = System.currentTimeMillis();

        System.out.println(end-start);

        ExecutorService es =  Executors.newFixedThreadPool(4);
        MyTask t1 = new MyTask(1,80000);
        MyTask t2 = new MyTask(80001,130000);
        MyTask t3 = new MyTask(130001,170000);
        MyTask t4 = new MyTask(170001,200000);
        //MyTask t5 = new MyTask(200000,300000);

        Future<List<Integer>> list1 = es.submit(t1);
        Future<List<Integer>> list2 = es.submit(t2);
        Future<List<Integer>> list3 = es.submit(t3);
        Future<List<Integer>> list4 = es.submit(t4);
        //Future<List<Integer>> list5 = es.submit(t5);

        List<Integer> all  = new ArrayList<>();

        try {
            all.addAll(list1.get());
            all.addAll(list2.get());
            all.addAll(list3.get());
            all.addAll(list4.get());
            //all.addAll(list5.get());
            System.out.println(all.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();
        System.out.println(end - start);

    }


}

class MyTask implements Callable<List<Integer>>{

    int start,end;

    public MyTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Integer> call() throws Exception {
        List<Integer> list = getPrime(this.start,this.end);
        return list;
    }

    public static List<Integer> getPrime(int start, int end) {
        List<Integer> results = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                results.add(i);
            }
        }
        return results;
    }

    public static boolean isPrime(int num){
        for (int i =2 ; i<= num/2 ;i++){
            if (num % i == 0){
                return false;
            }
        }
        return true;
    }
}
