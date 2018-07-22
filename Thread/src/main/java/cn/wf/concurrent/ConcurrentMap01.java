package cn.wf.concurrent;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

/**
 * 集合的高并发
 * Created by Mr_WF on 2018/7/22.
 */
public class ConcurrentMap01 {

    public static void main(String[] args) {
        // ConcurrentHashMap, ConcurrentSkipListMap
        //方式一使用:Hashtable 用时215ms左右，Hashtable是加了锁的，并发性不是很高的时候可以使用Hashtable
        //Map<String, String> maps = new Hashtable<>();
        //方式二使用:HashMap 用时230ms左右
        //Map<String, String> maps = new HashMap<>();
        //方式三使用:ConcurrentHashMap 在高并发的时候，ConcurrentHashMap的效率要比HashTable的高
        //Map<String, String> maps = new ConcurrentHashMap<>();

        //方式三使用:ConcurrentSkipListMap,调表数据结构map，高并发并且有序前提下，查询效率比较好
        Map<String, String> maps = new ConcurrentSkipListMap<>();

        Random random = new Random();
        Thread[] ths = new Thread[100];
        CountDownLatch countDownLatch = new CountDownLatch(ths.length);
        long start = System.currentTimeMillis();
        System.out.println("开始时间->"+start);
        for (int i = 0; i < ths.length; i++) {
            ths[i]= new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    maps.put("key-"+random.nextInt(100000),"value-"+random.nextInt(100000));
                    countDownLatch.countDown();
                }
            });
        }

        Arrays.asList(ths).forEach(t -> t.start());

        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("结束时间->"+start);
        System.out.println("历时->"+(end-start));
    }
}
