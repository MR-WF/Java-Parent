package cn.wf.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Mr_WF on 2018/7/22.
 */
public class QueueTest {
    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < 10; i++) {
            //strs.add("va;ue-"+i);
            //可返回添加有没有成功
            strs.offer("va;ue-"+i);
        }

    }

}
