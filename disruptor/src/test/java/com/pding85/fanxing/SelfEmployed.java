package com.pding85.fanxing;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

// java 指针赋值原子性问题 可能会有null -> 回收问题
// Java 不停的栈空间问题 set 问题
// 引用占有堆吗
public class SelfEmployed extends Sellers<Orange>
{

    volatile Apple app = new Apple();

    Thread t1 = new Thread() {

        @Override
        public void run() {
            while (true) {
                try {
                    app.isFlag();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };



    Thread t2 = new Thread() {

        @Override
        public void run() {
            while (true) {

                {app = new Apple(); }
                try {
                   System.gc();
                } catch (Exception e) {}
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        SelfEmployed t = new SelfEmployed();
        t.t1.start();
        t.t2.start();

        t.t1.join();
        t.t2.join();

        AtomicLong atomLong;
    }


}