package cn.wf.threadpool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * 第五种线程池  工作窃取线程池  当一个线程的线程等待的队列任务执行完毕，回去其他线程的队列中窃取任务来做
 * 底层是ForkJoinPool（分支合并线程池）
 * ****************************************************************************************************
 * 第六种线程池  分支合并线程池 分而治之，并行执行任务
 *  ForkJoinPool主要用来使用分治法(Divide-and-Conquer Algorithm)来解决问题。
 *  典型的应用比如快速排序算法。这里的要点在于，ForkJoinPool需要使用相对少的线程来处理大量的任务。
 *  比如要对1000万个数据进行排序，那么会将这个任务分割成两个500万的排序任务和一个针对这两组500万数据的合并任务。
 *  以此类推，对于500万的数据也会做出同样的分割处理，到最后会设置一个阈值来规定当数据规模到多少时，停止这样的分割处理。
 *  比如，当元素的数量小于10时，会停止分割，转而使用插入排序对它们进行排序。
 *  那么到最后，所有的任务加起来会有大概2000000+个。
 *  问题的关键在于，对于一个任务而言，只有当它所有的子任务完成之后，它才能够被执行。
 *  所以当使用ThreadPoolExecutor时，使用分治法会存在问题，因为ThreadPoolExecutor中的线程无法像任务队列中再添加一个任务并且在等待该任务完成之后再继续执行。
 *  而使用ForkJoinPool时，就能够让其中的线程创建新的任务，并挂起当前的任务，此时线程就能够从队列中选择子任务执行
 *
 *
 *  ForkJoinPool线程池 的.execute（task）的入参任务task必须是抽象的ForkJoinTask<?>类型，任务会自动切分。一般简单做法我们将自定义的Task继承自ForkJoinTask<?>的两个子类：
 *          1.RecursiveAction  （递归Action，类似于递归可一直切分） 该类的compute没有返回值
 *          2.RecursiveTask<T>  （递归任务，类似于递归可一直将任务进行切分） 该类的compute有返回值类型T 例如RecursiveTask<Long> 返回Long类型
 *
 * Created by Mr_WF on 2018/7/28.
 */
public class ForkJoinPoolTest {
    static int[] nums = new int[1000000];
    static final int MAX_NUM = 50000;
    static Random r = new Random();

    static {
        for(int i=0; i<nums.length; i++) {
            nums[i] = r.nextInt(100);
        }
        //计算产生的1000000个100以内随机数的和
        System.out.println(Arrays.stream(nums).sum()); //stream api
    }

    /**
     *
     */
    static class AddTaskNoReturn extends RecursiveAction{

        int start, end;

        AddTaskNoReturn(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            //小于设定分割数据量
            if (end - start <= MAX_NUM){
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += nums[i];
                }
                System.out.println("from:" + start + " to:" + end + " = " + sum);
            }else {
                int middle = start + (end-start)/2;
                AddTaskNoReturn subTask1 = new AddTaskNoReturn(start, middle);
                AddTaskNoReturn subTask2 = new AddTaskNoReturn(middle, end);
                //启动新的线程，子任务
                subTask1.fork();
                //启动新的线程，子任务
                subTask2.fork();
            }

        }
    }

    static class AddTask extends RecursiveTask<Long>{

        int start, end;

        AddTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= MAX_NUM){
                long sum =0;
                for (int i = start; i < end; i++) {
                    sum += nums[i];
                }
                return sum;
            }else {
                int middle = start + (end-start)/2;
                AddTask subTask1 = new AddTask(start, middle);
                AddTask subTask2 = new AddTask(middle, end);
                //启动新的线程，子任务
                subTask1.fork();
                //启动新的线程，子任务
                subTask2.fork();
                return subTask1.join() + subTask2.join();
            }
        }
    }


    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        /*AddTaskNoReturn task = new AddTaskNoReturn(0,nums.length);
        forkJoinPool.execute(task);

        try {
            //daemon线程 由于产生的是精灵线程（守护线程、后台线程），主线程不阻塞的话，看不到输出
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        AddTask task1 = new AddTask(0,nums.length);
        forkJoinPool.execute(task1);
        //join()本身就是阻塞的，所以不再需要 System.in.read();
        long result = task1.join();

        System.out.println(result);


    }

}
