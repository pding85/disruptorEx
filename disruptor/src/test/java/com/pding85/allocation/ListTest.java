package com.pding85.allocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ListTest {

    static Map<String, List<String>> container = new ConcurrentHashMap<>();
    static String KEY = "KEY-001" ;


    public static void iteration1(List<String> data) {

        if (data!=null && data.size() > 0) {
            container.put(KEY, data);
        }

        int size = 0;
        try {
            int len = 1;
            for (String ddd : data) {
                if (++len % 600 == 0) {
                    sleep2(1);
                }
                // sleep2(1);
                ++size;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(size);
    }


    public static void iteration2() {
        //
        List<String> valueList = container.get(KEY);
       //
        for (int i = 0 ; i < 100; ++i) {
            valueList.add("@222222");
        }


        iteration1(valueList);
    }

    public static void sleep1(int mill) {
        try {
            TimeUnit.MILLISECONDS.sleep(mill);
        } catch (Exception e) {}
    }

    public static void sleep2(int mill) {
        try {
            TimeUnit.NANOSECONDS.sleep(mill * 1);
        } catch (Exception e) {}
    }

    public static List<String> newData() {
        List<String> data = new ArrayList<>();
        for (int i = 0 ; i < 1000; ++i) {
            data.add(i + "");
        }

        return data;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {

                while (true) {

                    try {

                        ListTest.iteration1(newData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        sleep1(1);
                    }
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                sleep1(10);
                while (true) {

                    try {

                        iteration2();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        sleep1(1);
                    }
                }
            }
        };

        t1.start();
        t2.start();

        t1.join();

    }
}
