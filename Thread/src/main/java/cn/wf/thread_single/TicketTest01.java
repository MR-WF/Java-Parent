package cn.wf.thread_single;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * List不是同步的，不加锁会读写出现问题
 * Vector 是一个同步的容器,但如果不能保证方法的原子性，还是有读写问题
 * Created by Mr_WF on 2018/7/22.
 */
public class TicketTest01 {

    //static List<String> tickets = new ArrayList<>();
    static Vector<String> tickets = new Vector<>();
    static {
        for (int i = 0; i < 1000; i++) {
            tickets.add("票"+i);
        }
    }



    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true){
                    synchronized (tickets){
                        if (tickets.size()<=0){
                            break;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(1);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        System.out.println("消费了--"+tickets.remove(0));
                    }
                }
            }).start();
        }
    }


}
