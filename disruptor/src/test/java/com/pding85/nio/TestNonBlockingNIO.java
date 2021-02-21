package com.pding85.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/*
 * 使用 NIO 完成网络通信的三个核心：
 *
 * 1. 通道（Channel）：负责连接
 *        java.nio.channels.Channel 接口：
 *             |--SelectableChannel
 *                 |--SocketChannel
 *                 |--ServerSocketChannel
 *                 |--DatagramChannel
 *
 *                 |--Pipe.SinkChannel
 *                 |--Pipe.SourceChannel
 *
 * 2. 缓冲区（Buffer）：负责数据的存取
 *
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 * 可以监听的事件类型（可使用 SelectionKey 的四个常量表示）
 *     读: SelectionKey.OP_READ  （1）
 *     写: SelectionKey.OP_WRITE    （4）
 *     连接: SelectionKey.OP_CONNECT（8）
 *     接收: SelectionKey.OP_ACCEPT  （16）
 *
 * Selector 常用方法
 * Set<SelectionKey> keys()：所有的 SelectionKey 集合。代表注册在该 Selector上的 Channel
 * selectedKeys()：被选择的 SelectionKey 集合。返回此Selector的已选择键集
 * intselect()：监控所有注册的 Channel，当它们中间有需要处理的 IO 操作时，该方法返回，并将对应得的 SelectionKey 加入被选择的 SelectionKey 集合中，该方法返回这些 Channel 的数量。
 * int select(long timeout)：可以设置超时时长的 select() 操作
 * int selectNow()：执行一个立即返回的 select() 操作，该方法不会阻塞线程
 * Selector wakeup()：使一个还未返回的 select() 方法立即返回
 * void close()：关闭该选择器
 */
public class TestNonBlockingNIO {

    //客户端
    @Test
    public void client() throws IOException {
        // 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 切换非阻塞模式
        sChannel.configureBlocking(false);

        // 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 发送数据给服务端
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            String str = scan.next();
            buf.put((new Date().toString() + "\n" + str).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        // 关闭通道
        sChannel.close();
    }

    //服务端
    @Test
    public void server() throws IOException {
        // 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 切换非阻塞模式
        ssChannel.configureBlocking(false);

        // 绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        // 获取选择器
        Selector selector = Selector.open();

        // 将通道注册到选择器上, 并且指定“监听接收事件”
        ssChannel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_READ);

        // 轮询式的获取选择器上已经“准备就绪”的事件
        // 本质上 selector.select() 还是阻塞的
        while (selector.select(1000) > 0) {

            // 获取当前选择器中所有注册的“选择键(已就绪的监听事件)”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                // 获取准备“就绪”的是事件
                SelectionKey sk = it.next();

                // 判断具体是什么事件准备就绪
                if (sk.isAcceptable()) {
                    // 若“接收就绪”，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();

                    // 切换非阻塞模式
                    sChannel.configureBlocking(false);

                    // 将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    // 获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();

                    // 读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }

                // 移除当前 SelectionKey
                it.remove();
            }
        }
    }
}
