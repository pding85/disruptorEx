package com.pding85.allocation;

import io.netty.util.collection.IntObjectHashMap;

public class IntHashMapTest {

    public static void main(String[] args) {
        IntObjectHashMap map = new IntObjectHashMap();
        for (int i =0 ; i < 10000; ++i) {
            map.put(i, new String("11111" + i).intern());
        }

        System.out.println(map);
    }

}
