package cn.wf.concurrent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 写时赋值，读的效率高
 * Created by Mr_WF on 2018/7/22.
 */
public class CopyOnWriteListTest {


    public static void main(String[] args) {

        //不加锁，会出现并发问题
        // List<String> lists = new ArrayList<>();
        //加了锁
        //List<String> lists = new Vector<>();

        //实例在写的时候将容器赋值一份操作，然后将引用指向新的地址，这样读的时候不用加锁，但是写的效率低
        List<String> lists = new CopyOnWriteArrayList<>();

        Random r =new Random();
        Thread[] ths = new Thread[100];
        for (int i = 0; i < ths.length; i++) {
            Runnable t = () -> {
                for (int j = 0; j < 1000; j++) {
                    lists.add("value"+r.nextInt(100));
                }
            };
            ths[i] = new Thread(t);
        }

        long start = System.currentTimeMillis();
        Arrays.asList(ths).forEach(t->t.start());
        Arrays.asList(ths).forEach(ts -> {
            try{
                ts.join();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        System.out.println(lists.size());

    }

    /**
     * 将普通list加锁
     * Collections.synchronizedXXX 有List,也有Map，Set等
     * @param list
     * @return
     */
    public List<String> getSynList(List<String> list){
        return Collections.synchronizedList(list);
    }

}
