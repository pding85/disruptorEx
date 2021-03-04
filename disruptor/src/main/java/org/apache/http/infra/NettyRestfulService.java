package org.apache.http.infra;

import com.google.common.base.Strings;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.infra.pipeline.RestfulServiceChannelInitializer;

/**
 * Implemented {@link RestfulService} via Netty.
 */
@Slf4j
@RequiredArgsConstructor
public final class NettyRestfulService implements RestfulService {

    private static final int DEFAULT_WORKER_GROUP_THREADS = 1 + 2 * NettyRuntime.availableProcessors();

    private final NettyRestfulServiceConfiguration configuration;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossEventLoopGroup;

    private EventLoopGroup workerEventLoopGroup;

    private void initServerBootstrap() {
        bossEventLoopGroup = new NioEventLoopGroup();
        workerEventLoopGroup = new NioEventLoopGroup(DEFAULT_WORKER_GROUP_THREADS);
        serverBootstrap = new ServerBootstrap()
                .group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RestfulServiceChannelInitializer(configuration));
    }

    @SneakyThrows
    @Override
    public void startup() {
        initServerBootstrap();
        ChannelFuture channelFuture;
        if (!Strings.isNullOrEmpty(configuration.getHost())) {
            channelFuture = serverBootstrap.bind(configuration.getHost(), configuration.getPort());
        } else {
            channelFuture = serverBootstrap.bind(configuration.getPort());
        }
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                log.info("Restful Service started on port {}.", configuration.getPort());
            } else {
                log.error("Failed to start Restful Service.", future.cause());
            }
        }).sync();
    }

    @Override
    public void shutdown() {
        bossEventLoopGroup.shutdownGracefully();
        workerEventLoopGroup.shutdownGracefully();
    }
}