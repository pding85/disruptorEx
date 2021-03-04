package com.pding85.allocation;

import clojure.main;
import io.netty.util.internal.ConstantTimeUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *非阻塞 Map
 * ConcurrentAutoTable（后面几个map/set结构的基础）
 * NonBlockingHashMap
 * NonBlockingHashMapLong
 * NonBlockingHashSet
 * NonBlockingIdentityHashMap
 * NonBlockingSetInt
 *非阻塞 Queue
 * JCTools 提供的非阻塞队列分为 4 类，可以根据不同的应用场景选择使用：
 *
 * SPSC-单一生产者单一消费者（有界和无界）
 * MPSC-多生产者单一消费者（有界和无界）
 * SPMC-单生产者多消费者（有界）
 * MPMC-多生产者多消费者（有界）
 */
public class JCToolTest {

    public static void main(String[] args) {

        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

        Thread t1 = new Thread() {
            @Override
            public void run() {


            }
        };




    }

}
