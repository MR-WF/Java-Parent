package cn.wf.thread01;

/**
 * 该类提供了线程局部 (thread-local) 变量。这些变量不同于它们的普通对应物，因为访问某个变量（通过其 get 或 set 方法）的每个线程都有自己的局部变量，
 * 它独立于变量的初始化副本。
 * ThreadLocal实例通常是类中的 private static 字段，它们希望将状态与某一个线程（例如，用户 ID 或事务 ID）相关联
 * ---------------------------------------------------------------------------------------------------------------------------------
 * 所以，ThreadLocal与线程同步机制不同，线程同步机制是多个线程共享同一个变量，而ThreadLocal是为每一个线程创建一个单独的变量副本，
 * 故而每个线程都可以独立地改变自己所拥有的变量副本，而不会影响其他线程所对应的副本。可以说ThreadLocal为多线程环境下变量问题提供了另外一种解决思路
 * @description:
 * @author: it.wf
 * @create: 2018-07-20 14:19
 **/
public class ThreadTest01 {

    private static ThreadLocal<Integer> tCount = new ThreadLocal<Integer>(){
        //返回此线程局部变量的当前线程的“初始值”
        @Override
        protected Integer initialValue() {
            return 0;
        }

        //返回此线程局部变量的当前线程副本中的值
        @Override
        public Integer get() {
            return super.get();
        }

        //将此线程局部变量的当前线程副本中的值设置为指定值。
        @Override
        public void set(Integer value) {
            super.set(value);
        }

        //移除此线程局部变量当前线程的值
        @Override
        public void remove() {
            super.remove();
        }
    };

    /**
     * 获取下一个
     * @return
     */
    public int nextSeq(){
        tCount.set(tCount.get() + 2);
        return tCount.get();
    }

    public static void main(String[] args) {
        ThreadTest01 t = new ThreadTest01();
        SeqThread thread = new SeqThread(t);
        SeqThread thread1 = new SeqThread(t);
        SeqThread thread2 = new SeqThread(t);
        SeqThread thread3 = new SeqThread(t);
        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();



    }

    static class SeqThread extends Thread{
        private ThreadTest01 tt;
        SeqThread(ThreadTest01 threadTest01){
            this.tt = threadTest01;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5;i++){
                System.out.println(Thread.currentThread().getName()+"******"+tt.nextSeq());
            }
        }
    }

}
