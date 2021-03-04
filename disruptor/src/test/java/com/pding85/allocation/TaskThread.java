package com.pding85.allocation;

import org.apache.commons.lang.time.StopWatch;
import org.jctools.queues.MpscLinkedQueue;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//  MpscLinkedQueue 和 ConcurrentLinkedQueue 性能比较
public class TaskThread extends Thread  {

    Lock lock = new ReentrantLock();
    Condition signal = lock.newCondition();
    Queue<Long> queue = new MpscLinkedQueue<>();
    static Unsafe unsafe;
    static final Long count = 1000_000L;
    AtomicBoolean status = new AtomicBoolean(false);

    static {

        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {}
    }


    public void park() {
        try {
            lock.lock();
            signal.wait();
        } catch (Exception e) {

        } finally {
            try{
                lock.unlock();
            } catch (Exception e) {}
        }
    }

    public void park2() {

        unsafe.park(false, 0);
    }

    public void offer2(Long sequence) {
        try {
            queue.offer(sequence);

                // unsafe.unpark(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void offer(Long sequence) {
        try {
            lock.lock();
            queue.offer(sequence);
            signal.signalAll(); // 不是每次必须的，浪费性能
        } catch (Exception e) {

        } finally {
            try{
                lock.unlock();
            } catch (Exception e) {}
        }
    }

    @Override
    public void run() {
        long i = 0L ;
        while (true) {

            try {
                if (queue.isEmpty()) {
                    park();
                }

                Long data = queue.poll();

                if (data % 10000 ==0)
                    System.out.println(data);

                if (count <= data) {
                    return;
                }


            } catch (Exception e) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (Exception exception) {}
            }

        }
    }

    // 测试代码
    public static void main(String[] args) {
        //
        TaskThread center = new TaskThread();

        Thread producer = new Thread() {
            volatile  Long i = 0L;

            @Override
            public void run() {

                while (true) {

                    center.offer(++i);
                    if (i >= count) {
                        return;
                    }
                }
            }
        };
        producer.start();
        center.start();


        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            producer.join();
        } catch (Exception e) {}

        try {
           center.join();
        } catch (Exception e) {}

        stopWatch.stop();
        System.out.println(stopWatch.getTime());

    }
}
