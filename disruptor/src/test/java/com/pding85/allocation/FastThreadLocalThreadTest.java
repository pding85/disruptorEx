package com.pding85.allocation;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class FastThreadLocalThreadTest {

    public static void main(String ...s) {
        final int threadLocalCount = 20;
        final FastThreadLocal<String>[] caches = new FastThreadLocal[threadLocalCount];
        final Thread mainThread = Thread.currentThread();
        for (int i=0;i<threadLocalCount;i++) {
            caches[i] = new FastThreadLocal();
        }
        Thread t = new FastThreadLocalThread(new Runnable() {
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
