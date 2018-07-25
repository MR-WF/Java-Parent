package cn.wf.thread01;

import java.util.concurrent.TimeUnit;

/**
 * synchronized 可以保证方法或者代码块在运行时，
 * 同一时刻只有一个方法可以进入到临界区，同时它还可以保证共享变量的内存可见性。深入分析 synchronized 的内在实现机制，锁优化、锁升级过程
 *
 * synchronized优化
 * 同步代码块中的语句越少越好
 * 根据业务逻辑中只有需要的地方加sync，这时不应该给整个方法上锁
 * 采用细粒度的锁，可以使线程争用时间变短，从而提高效率
 *
 * @description:
 *       1:同步和非同步方法可以同时调用
 *       2. Lambda表达式实例
 * @author: it.wf
 * @create: 2018-07-25 10:51
 **/
public class Base implements Runnable{
    private int count = 0;

    /**
     * 加锁synchronized保证run()操作的原子性
     */
    @Override
    public /*synchronized*/ void run() {
        count++;
        System.out.println(Thread.currentThread().getName() + "--count--" + count);
    }

    public static void main(String[] args) {
        Base base = new Base();
        for (int i = 0; i < 100; i++) {
            new Thread(base,"Thread*"+i).start();
        }
    }

    /**
     * 同步和非同步方法可以同时调用
     * 因为非同步方法不需要 申请 同步方法锁定的资源（对象）即可执行，它不关心
     */
    private synchronized void t1(){
        System.out.println(Thread.currentThread().getName() + "synchronized同步方法t1****start...");
        try {
            //Thread.sleep(10000);
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "synchronized同步方法t1****end");
    }

    private  void t2(){
        System.out.println(Thread.currentThread().getName() + "非同步方法t2****start...");
        try {
            //Thread.sleep(10000);
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "非同步方法t2****end");
    }

    private void test(){
        Base b = new Base();
        /**
         * 启动线程，原始写法
         */
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                b.t1();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                b.t2();
            }
        });*/
        /**
         * 启动线程， Lambda表达式 - 普通
         */
        //new Thread(()->b.t1()).start();
        //new Thread(()->b.t2()).start();

        /**
         * 启动线程， Lambda表达式 - ::
         */
        new Thread(b::t1).start();
        new Thread(b::t2).start();

    }
}
