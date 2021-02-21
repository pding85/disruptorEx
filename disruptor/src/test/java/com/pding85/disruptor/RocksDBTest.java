package com.pding85.disruptor;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;

public class RocksDBTest {

    static{
        RocksDB.loadLibrary();
    }

    static RocksDB rocksDB;
    static String path = "F:\\tmp\\rockdb\\db" ;
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.setCreateIfMissing(true);
        rocksDB = RocksDB.open(options, path);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000000; ++i) {
            rocksDB.put(("key_" + i).getBytes(), (i + "在Java环境下使用RocksDB需要下载其jar包，使用maven工程的pom,.xml文件进行配置：在Java环境下使用RocksDB需要下载其jar包，使用maven工程的pom,.xml文件进行配置：在Java环境下使用RocksDB需要下载其jar包，使用maven工程的pom,.xml文件进行配置：在Java环境下使用RocksDB需要下载其jar包，使用maven工程的pom,.xml文件进行配置：在Java环境下使用RocksDB需要下载其jar包，使用maven工程的pom,.xml文件进行配置：_" + i).getBytes());
        }

        long end = System.currentTimeMillis();
        System.out.println("寫入耗時:" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            String dd = new String(rocksDB.get(("key_" + i).getBytes()));
        }
        end = System.currentTimeMillis();
        System.out.println("讀取耗時:" + (end - start));
    }
}
