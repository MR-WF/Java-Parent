package cn.wf.thread01.example;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 *   银行账户:一般需要对写和读都加锁，否则程序的可能出现数据脏读问题
 * @author: it.wf
 * @create: 2018-07-25 12:29
 **/
public class BankAccount {
    //账户名
    String name;
    //账户余额
    double  balance;


    private synchronized void setBalance(String name,double balance){
        this.name = name;
        //如果读的方法不加锁，当此处操作存在延迟，则很容易重现线程读取到的数据不对
        try {
            TimeUnit.MILLISECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance = balance;
        System.out.println("name->"+this.name+"---balance->"+this.balance);
    }
    private /*synchronized*/ double getBalance(String name){
        return this.balance;
    }

    public static void main(String[] args) {
        BankAccount bk = new BankAccount();
        new Thread(()->bk.setBalance("11",10000.0)).start();
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(bk.getBalance("11"));

        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(bk.getBalance("11"));
    }


}
