package cn.wf.thread01;

/**
 * @description: synchronized基础概念
 * @author: it.wf
 * @create: 2018-07-24 21:33
 **/
public class ThreadBase {

    private int num = 10;

    private static int num2 = 10;

    /**
     * synchronized基础 :
     * java是面试对象的编程语言
     *  如果synchronized加在普通方法上锁定的此类的实例对象，任何线程要执行下面的代码，必须先拿到该实例对象(this)的锁
     *  而如果synchronized加在static方法上，static方法不属于某个对应而是属于类T.class,所以synchronized加在static方法上锁定的是T.class
     */

    /**
     * synchronized(o)或者synchronized(this)
     * synchronized(this)等价与private synchronized void xxx()｛｝
     */
    //private Object o = new Object();
    private void synchronized01() {
        //synchronized (o){
        synchronized (this) {
            num--;
            System.out.println(Thread.currentThread().getName() + "--num" + num);
        }
    }

    private synchronized void synchronized02() {
        num--;
        System.out.println(Thread.currentThread().getName() + "--num" + num);
    }


    /**
     * 这里等同于synchronized(ThreadBase.class)
     */
    private synchronized static void staticSynchronized(){
        num2--;
        System.out.println(Thread.currentThread().getName() + "--num2-" + num2);
    }

    /**
     * 等价上面的写法
     */
    private  static void staticSynchronized2(){
        synchronized (ThreadBase.class){
            num2--;
            System.out.println(Thread.currentThread().getName() + "--num2-" + num2);
        }
    }

    public static void main(String[] args) {
        //staticSynchronized2();
    }




}
