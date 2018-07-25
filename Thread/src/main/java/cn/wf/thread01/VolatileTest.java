package cn.wf.thread01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 *   volatile 可以保证线程可见性且提供了一定的有序性，但是无法保证原子性。在 JVM 底层 volatile 是采用“内存屏障”来实现的
 *      1.volatile的内存语义是：
 *          当写一个 volatile 变量时，JMM 会把该线程对应的本地内存中的共享变量值立即刷新到主内存中。
 *          当读一个 volatile 变量时，JMM 会把该线程对应的本地内存设置为无效，直接从主内存中读取共享变量
 *
 *      2.volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized
 *         synchronized可以同时保证可见性和原子性，volatile只能保证可见性\
 *
 *     3 java提供了具有同时原子性和可见性的的包装类   以Atomic***开头，例如AtomicInteger,AtomicBoolean,AtomicLong等
 *
 * @author: it.wf
 * @create: 2018-07-25 19:49
 **/
public class VolatileTest {

    //volatile int num = 0;
    //保障可见性还有另一种更好的解决方式
    /**
     * java提供了具有同时原子性和可见性的的包装类   以Atomic***开头，例如AtomicInteger,AtomicBoolean,AtomicLong等
     */
    AtomicInteger num = new AtomicInteger(0);

    private /*synchronized*/ void t1(){
        for (int i = 0; i < 1000; i++) {
            //num++;
            num.incrementAndGet();
            //System.out.println("第"+i+"次调用,当前i："+i);
        }
    }

    public static void main(String[] args) {
        VolatileTest v = new VolatileTest();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(v::t1,"线程"+i));
        }
        threads.forEach((o)->o.start());

        threads.forEach((o)->{
            try {
                //会中断原有线程
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(v.num);
    }
}
