package com.pding85.allocation;

import com.pding85.fanxing.Apple;
import org.apache.ignite.internal.processors.cache.persistence.wal.SegmentedRingByteBuffer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class DoubleCache<T> {

    public static final Byte EMPTY = Byte.MIN_VALUE;

    private List<ConcurrentHashMap<T, Byte>> cache;

    int size = 2;

    private volatile int index = 0 ;

    private static final AtomicIntegerFieldUpdater<DoubleCache> INDEX_UPD =
            AtomicIntegerFieldUpdater.newUpdater(DoubleCache.class, "index");

    public DoubleCache() {
        index = 0 ;
        cache = new ArrayList<>(size);
        for (int i = 0 ; i < size; ++i) {
            ConcurrentHashMap<T, Byte> map = new ConcurrentHashMap(1 << 10);
            cache.add(map);
        }
    }

    public void write(Set<T> values) {
        int currentIndex = INDEX_UPD.get(this);
        ConcurrentHashMap<T, Byte> map = cache.get(currentIndex);
        for (T key : values) {
            if (!map.containsKey(key)) {
                map.put(key, EMPTY);
            }
        }
    }

    public Set<T> read() {
        Set<T> data = new HashSet<>();
        int currentIndex = INDEX_UPD.get(this);
        int next =  currentIndex ^ 0x01;

        // 先判断next对应的容器是否为空
        ConcurrentHashMap<T, Byte> last = cache.get(next);
        if (last.isEmpty()) {
            System.out.println("上一次遍历完");
        } else {
            System.out.println("上一次遍历 not over");
        }

        for (;;) {
            if (INDEX_UPD.compareAndSet(this, currentIndex, next)) {
                break;
            }
        }

        ConcurrentHashMap<T, Byte> map = cache.get(currentIndex);
        java.util.Iterator<Map.Entry<T, Byte>> iterator =  map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<T, Byte> ent = iterator.next();
            data.add(ent.getKey());
            iterator.remove();
        }
        return data;
    }

    ///  ----  测试代码 ------------
    public static void main(String[] args) throws InterruptedException {

        Set<String> container = new HashSet<>();
        for (int i = 0; i < 10000; ++i) {
            container.add(i + "");
        }

        // 适用场景， 写多读少的场景
        DoubleCache<String> cache = new DoubleCache<>();

        Thread t1 = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        cache.write(container);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };



        Thread t2 = new Thread() {

            @Override
            public void run() {
                int batch = 1;
                while (true) {
                    try {
                        Set<String> data = cache.read();
                        System.out.println("batch:" + batch + ", size:" + data.size());
                        ++batch;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (Exception e) {}
                    }
                }
            }
        };

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
