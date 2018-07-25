package cn.wf.thread01;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description:
 *  锁定某对象o，如果o的属性发生改变，不影响锁的使用
 *          但是如果o变成另外一个对象，则锁定的对象发生改变.锁定的是对象的引用地址
 *          应该避免将锁定对象的引用变成另外的对象
 * ****************************************************************************************
 * 不要以字符串常量作为锁定对象
 *   String s1 = "Hello";
 *   String s2 = "Hello";

    void m1() {synchronized(s1) {}}
    void m2() {synchronized(s2) {}}

    m1和m2其实锁定的是同一个对象
 * 比如你用到了一个类库，在该类库中代码锁定了字符串“Hello”，
 * 但是你读不到源码，所以你在自己的代码中也锁定了"Hello",这时候就有可能发生非常诡异的死锁阻塞，
 * 因为你的程序和你用到的类库不经意间使用了同一把锁
 * @author: it.wf
 * @create: 2018-07-25 19:35
 **/
public class SynObjectTest {

    AtomicBoolean b = new AtomicBoolean(true);

    Studet s = new Studet("Tom",10);
    void t1(){
        synchronized (s){
            while (b.get()){
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                s.setAge(s.getAge()+1);
                System.out.println("当前线程为-->"+Thread.currentThread().getName()+"---s信息->"+s.toString());
               /* if (s.getAge()>1000){
                    b.set(false);
                }*/
            }
        }
    }

    public static void main(String[] args) {
        SynObjectTest t = new SynObjectTest();
        new Thread(t::t1,"线程1").start();
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread tt = new Thread(t::t1, "线程2");
        t.s.setAge(12);
        t.s.setName("Jerry");
        tt.start();

        Thread tt2 = new Thread(t::t1,"线程3");
        //锁对象发生改变，所以tt2线程得以执行，如果注释掉这句话，线程tt2将永远得不到执行机会
        t.s = new Studet("Haha",0);
        tt2.start();

    }

}

class Studet{
    private String name;
    private int age;

    public Studet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Studet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
