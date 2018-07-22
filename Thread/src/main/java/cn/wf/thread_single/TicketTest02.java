package cn.wf.thread_single;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * List不是同步的，不加锁会读写出现问题
 * Vector 是一个同步的容器,但如果不能保证方法内：判定和操作的原子性，还是有读写问题
 * Concurrent接口  1.5之后增加的并发性容器 Queue其中一种 ，不是枷锁的实现，效率比较高
 * Created by Mr_WF on 2018/7/22.
 */
public class TicketTest02 {

    static Queue<String> tickets = new ConcurrentLinkedQueue<>();
    static {
        for (int i = 0; i < 1000; i++) {
            tickets.add("票"+i);
        }
    }



    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true){
                    //poll()同步的拿出集合里的头元素,但有可能为null
                   String t = tickets.poll();
                    if (t!=null){
                        System.out.println("消费了-"+t);
                    }else {
                        break;
                    }
                }
            }).start();
        }
    }


}
