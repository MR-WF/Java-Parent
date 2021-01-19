package cn.wf.listtest;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: it.wf
 * @create: 2019-12-31 11:20
 **/
public class AlibabTest {
    public static void main(String[] args) {
        AlibabTest alibabTest = new AlibabTest();
        alibabTest.testList();
    }

    void testList(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println(list.toString());

        for (String str:list){
            if ("3".equals(str)){
                System.out.println("移除对象"+str);
                list.remove(str);
            }
        }
        System.out.println(list.toString());


    }
}
