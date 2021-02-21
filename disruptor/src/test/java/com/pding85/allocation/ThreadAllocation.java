package com.pding85.allocation;

import java.util.*;

public class ThreadAllocation {

    // 当前线程数量总和
    private int threadCnt ;

    // 当前股票列表
    private List<String> stockList ;

    // 当前股票对应的报文更新量
    private Map<String, Long> stockStatics;

    private Map<String, Byte> allocate;

    public ThreadAllocation(int threadCnt,
                            List<String> stockList,
                            Map<String, Long> stockStatics) {
        this.threadCnt = threadCnt;
        this.stockList = stockList;
        this.stockStatics = stockStatics;
        allocate = new HashMap<>();
    }

    public void allocateThread() {
        List<Map.Entry<String,Long>> list =
                new ArrayList<Map.Entry<String,Long>>(stockStatics.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Long>>() {
            //降序排序
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int index = 0;
        for (Map.Entry<String,Long> stock : list) {
            int threadId = index % threadCnt;
            allocate.put(stock.getKey().intern(), (byte) threadId);
            ++ index;
        }
    }

    public int getHash(String code) {
        return allocate.get(code);
    }
}
