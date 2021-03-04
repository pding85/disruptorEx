package com.pding85.allocation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class JDKThreadLocalThreadTest {

    public static void main(String ...s) {
        final int threadLocalCount = 20;
        final ThreadLocal<String>[] caches = new ThreadLocal[threadLocalCount];
        final Thread mainThread = Thread.currentThread();
        for (int i=0;i<threadLocalCount;i++) {
            caches[i] = new ThreadLocal();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<threadLocalCount;i++) {
                    caches[0].set("float.lu");
                }
                long start = System.nanoTime();
                for (int i=0;i<threadLocalCount;i++) {
                    for (int j=0;j<1000000;j++) {
                        caches[0].get();
                    }
                }
                long end = System.nanoTime();
                System.out.println("take[" + TimeUnit.NANOSECONDS.toMillis(end - start) +
                        "]ms");
                LockSupport.unpark(mainThread);
            }

        });
        t.start();
        LockSupport.park(mainThread);
    }
}
