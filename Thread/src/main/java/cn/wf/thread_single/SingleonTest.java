package cn.wf.thread_single;

/**
 * 不用枷锁，也不用懒加载，通过static内部类实现单例
 * Created by Mr_WF on 2018/7/22.
 */
public class SingleonTest {

    private SingleonTest(){
        System.out.println("SingleonTest构造");
    }
    private static class InnerClass{
        private static SingleonTest singleon = new SingleonTest();
    }

    private static SingleonTest getInstance(){
        return InnerClass.singleon;
    }


    public static void main(String[] args) {
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i]  = new Thread(() -> {
               SingleonTest.getInstance();
            });
        }
        //Arrays.asList(threads).forEach(o -> o.start());
        for (int i = 0; i < threads.length; i++) {
            System.out.println("第"+i+"次");
            threads[i].start();
        }
    }

}
