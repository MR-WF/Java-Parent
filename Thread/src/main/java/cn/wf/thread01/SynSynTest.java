package cn.wf.thread01;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * 在一个同步方法里可以去调用另外一个同步方法，如果两个线程申请的资源（对象）是同一个，
 * 则一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到该对象的锁
 * 也就是说synchronized获得的锁是可重入的
 * @author: it.wf
 * @create: 2018-07-25 14:13
 **/
public class SynSynTest {

    private synchronized void t1(){
        System.out.println(Thread.currentThread().getName()+"--t1--start");
        try {
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2();
        System.out.println(Thread.currentThread().getName()+"--t1--end");
    }

    protected synchronized void t2(){
        System.out.println(Thread.currentThread().getName()+"--t2--start");
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"--t2--end");
    }



    public static void main(String[] args) {
      /*  SynSynTest t = new SynSynTest();
        for (int i = 0; i < 5; i++) {
            //new Thread(t::t1).start();
            new Thread(()->t.t1()).start();
        }*/

        /**
         * 继承中有可能发生的情形:
         *     子类调用父类的同步方法
         */
      new SynSynChildren().t2();

    }
}

class SynSynChildren extends  SynSynTest{

    @Override
    protected synchronized void t2(){
        System.out.println(Thread.currentThread().getName()+"--ct2--start");
        super.t2();
        System.out.println(Thread.currentThread().getName()+"--ct2--end");
    }

}