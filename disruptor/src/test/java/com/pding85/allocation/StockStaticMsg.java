package com.pding85.allocation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class StockStaticMsg {


    public static void main(String[] args) {



        long start = System.currentTimeMillis();
        int cnt = 10000000;
        for (int i = 0 ; i < cnt; ++i) {
            LocalDateTime d1 = LocalDateTime.now().minusHours(1);
            LocalDateTime d2 = LocalDateTime.now().minusSeconds(22);
            compare3(d1,d2);
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start));
    }

    public static int compare1(LocalDateTime d1, LocalDateTime d2) {
        return d1.compareTo(d2);
    }

    public static long compare2(LocalDateTime d1, LocalDateTime d2) {
        Duration duration = Duration.between(d1, d2);
        return duration.toMillis();
    }


    static DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static long compare3(LocalDateTime d1, LocalDateTime d2) {
         long t1 =  d1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long t2 = d2.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return t1 - t2;
    }
}
