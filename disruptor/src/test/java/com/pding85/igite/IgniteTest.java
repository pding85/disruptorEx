package com.pding85.igite;


import org.apache.ignite.*;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteFuture;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  资料地址为： https://www.zybuluo.com/liyuj/note/963460
 *
 *ignite-spring：基于Spring的配置支持
 * ignite-indexing：SQL查询和索引
 * ignite-geospatial：地理位置索引
 * ignite-hibernate：Hibernate集成
 * ignite-web：Web Session集群化
 * ignite-schedule：基于Cron的计划任务
 * ignite-log4j：Log4j日志
 * ignite-jcl：Apache Commons logging日志
 * ignite-jta：XA集成
 * ignite-hadoop2-integration：HDFS2.0集成
 * ignite-rest-http：HTTP REST请求
 * ignite-scalar：Ignite Scalar API
 * ignite-slf4j：SLF4J日志
 * ignite-ssh；SSH支持，远程机器上启动网格节点
 * ignite-urideploy：基于URI的部署
 * ignite-aws：AWS S3上的无缝集群发现
 * ignite-aop：网格支持AOP
 * ignite-visor-console：开源的命令行管理和监控工具
 *
 * mvn clean install -DskipTests -Plgpl -pl modules/hibernate -am
 */
public class IgniteTest {

    public static void main(String[] args) throws Exception {
        test001();
    }


    public static void test001() {
        try (Ignite ignite = Ignition.start("F:\\program\\java\\code\\apache-ignite-2.9.1-bin\\apache-ignite-2.9.1-bin\\examples\\config/example-ignite.xml")) {
            IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCacheName");
            // Store keys in cache (values will end up on different cache nodes).
            for (int i = 0; i < 10; i++)
                cache.put(i, Integer.toString(i));
            for (int i = 0; i < 10; i++)
                System.out.println("Got [key=" + i + ", val=" + cache.get(i) + ']');
        }
    }

    public static void create() throws Exception {
        // Register JDBC driver.
        Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
        // Open JDBC connection.
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/");
        // Create database tables.
        try (Statement stmt = conn.createStatement()) {
            // Create table based on REPLICATED template.
            stmt.executeUpdate("CREATE TABLE City (" +
                    " id LONG PRIMARY KEY, name VARCHAR) " +
                    " WITH \"template=replicated\"");
            // Create table based on PARTITIONED template with one backup.
            stmt.executeUpdate("CREATE TABLE Person (" +
                    " id LONG, name VARCHAR, city_id LONG, " +
                    " PRIMARY KEY (id, city_id)) " +
                    " WITH \"backups=1, affinityKey=city_id\"");
            // Create an index on the City table.
            stmt.executeUpdate("CREATE INDEX idx_city_name ON City (name)");
            // Create an index on the Person table.
            stmt.executeUpdate("CREATE INDEX idx_person_name ON Person (name)");
        }

        // Populate City table
        try (PreparedStatement stmt =
                     conn.prepareStatement("INSERT INTO City (id, name) VALUES (?, ?)")) {
            stmt.setLong(1, 1L);
            stmt.setString(2, "Forest Hill");
            stmt.executeUpdate();
            stmt.setLong(1, 2L);
            stmt.setString(2, "Denver");
            stmt.executeUpdate();
            stmt.setLong(1, 3L);
            stmt.setString(2, "St. Petersburg");
            stmt.executeUpdate();
        }

        // Populate Person table
        try (PreparedStatement stmt =
                     conn.prepareStatement("INSERT INTO Person (id, name, city_id) VALUES (?, ?, ?)")) {
            stmt.setLong(1, 1L);
            stmt.setString(2, "John Doe");
            stmt.setLong(3, 3L);
            stmt.executeUpdate();
            stmt.setLong(1, 2L);
            stmt.setString(2, "Jane Roe");
            stmt.setLong(3, 2L);
            stmt.executeUpdate();
            stmt.setLong(1, 3L);
            stmt.setString(2, "Mary Major");
            stmt.setLong(3, 1L);
            stmt.executeUpdate();
            stmt.setLong(1, 4L);
            stmt.setString(2, "Richard Miles");
            stmt.setLong(3, 2L);
            stmt.executeUpdate();
        }

        // Get data
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs =
                         stmt.executeQuery("SELECT p.name, c.name " +
                                 " FROM Person p, City c " +
                                 " WHERE p.city_id = c.id")) {
                while (rs.next())
                    System.out.println(rs.getString(1) + ", " + rs.getString(2));
            }
        }

        conn.close();

    }

    // 计算单词
    public static void wordCompute() {
        try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
            // Iterate through all the words in the sentence and create Callable jobs.
            for (final String word : "Count characters using callable".split(" "))
                calls.add(word::length);
            // Execute collection of Callables on the grid.
            Collection<Integer> res = ignite.compute().call(calls);
            // Add up all the results.
            int sum = res.stream().mapToInt(Integer::intValue).sum();
            System.out.println("Total number of characters is '" + sum + "'.");
        }
    }

    // ignite 启动方式
    public static void lifeCycleBean() {
        // Create new configuration.
        IgniteConfiguration cfg = new IgniteConfiguration();
        // Provide lifecycle bean to configuration.
        cfg.setLifecycleBeans(new LifecycleBean(){

            @Override
            public void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {
                System.out.println(evt);

                if (evt == LifecycleEventType.BEFORE_NODE_START) {
                    // Do something
                }

                /*
                    BEFORE_NODE_START：Ignite节点的启动程序初始化之前调用
                    AFTER_NODE_START：Ignite节点启动之后调用
                    BEFORE_NODE_STOP：Ignite节点的停止程序初始化之前调用
                    AFTER_NODE_STOP：Ignite节点停止之后调用
                 */
            }
        });
        // Start Ignite node with given configuration.
        Ignite ignite = Ignition.start(cfg);
    }

    // 下面的接口可以用于同步或者异步模式
    // IgniteCompute  IgniteCache  Transaction  IgniteServices IgniteMessaging IgniteEvents
    public static void test003() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        Ignite ignite = Ignition.start(cfg);
        IgniteCompute compute = ignite.compute();
        IgniteFuture<String> fut = compute.callAsync(() -> {return "Hello World";});
        fut.listen(f -> System.out.println("Job result: " + f.get()));
    }

    // 客户端 模式
    public static void test004() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        Ignite ignite = Ignition.start(cfg);

        // 方便起见，也可以通过Ignition类来打开或者关闭客户端模式作为替代，这样可以使客户端和服务端共用一套配置。
        // Ignition.setClientMode(true);
    }

    // 管理慢客户端
    public static void test005() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        TcpCommunicationSpi commSpi = new TcpCommunicationSpi();
        // 编程的方式配置慢客户端队列限值：
        commSpi.setSlowClientQueueLimit(1000);
        cfg.setCommunicationSpi(commSpi);
    }

    /**
     *
     * 有几种情况客户端会从集群中断开：
     *
     * 由于网络原因，客户端无法和服务端重建连接；
     * 与服务端的连接有时被断开，客户端也可以重建与服务端的连接，但是由于服务端无法获得客户端心跳，服务端仍然断开客户端节点；
     * 慢客户端会被服务端节点踢出；
     * 当一个客户端发现它与一个集群断开时，会为自己赋予一个新的节点id然后试图与该服务端重新连接。注意：这会产生一个副作用，就是当客户端重建连接时本地ClusterNode的id属性会发生变化，这意味着，如果业务逻辑依赖于这个id，就会受到影响。
     * 当客户端处于一个断开状态并且试图重建与集群的连接过程中时，Ignite API会抛出一个特定的异常：IgniteClientDisconnectedException，这个异常提供了一个future，当客户端重连成功后他会完成（IgniteCacheAPI会抛出CacheException，他有一个IgniteClientDisconnectedException作为他的cause）。这个future也可以通过IgniteCluster.clientReconnectFuture()方法获得。
     * 此外，客户端重连也有一些特定的事件（这些事件是本地化的，也就是说他们只会在客户端节点触发）：
     *
     * EventType.EVT_CLIENT_NODE_DISCONNECTED
     * EventType.EVT_CLIENT_NODE_RECONNECTED
     */
    public static void test006() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        Ignite ignite = Ignition.start(cfg);
        IgniteCompute compute = ignite.compute();

        while (true) {
            try {
                compute.run(new IgniteRunnable(){

                    @Override
                    public void run() {
                        //
                        System.out.println("111");
                    }
                });
            }
            catch (IgniteClientDisconnectedException e) {
                e.reconnectFuture().get(); // Wait for reconnection.
                // Can proceed and use the same IgniteCompute instance.
            }
        }
    }

    public static void test007() {
        Ignite ignite = Ignition.ignite();
        Collection<String> res = ignite.compute().broadcast(new IgniteCallable<String>() {
            // Inject Ignite instance.
            @IgniteInstanceResource
            private Ignite ignite;
            @Override
            public String call() throws Exception {
                IgniteCache<Object, Object> cache = ignite.getOrCreateCache("CACHE_NAME");
                // Do some stuff with cache.
                return "OK";
            }
        });
    }

    //
    public static void test008() {
        Ignite ignite = Ignition.ignite();
        IgniteBinary binary = ignite.binary();

    }

}
