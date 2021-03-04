package org.apache.http.infra.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.apache.http.infra.NettyRestfulServiceConfiguration;

/**
 * Initialize channel pipeline.
 */
public final class RestfulServiceChannelInitializer extends ChannelInitializer<Channel> {

    private final ContextInitializationInboundHandler contextInitializationInboundHandler;

    private final FilterChainInboundHandler filterChainInboundHandler;

    private final HttpRequestDispatcher httpRequestDispatcher;

    private final HandlerParameterDecoder handlerParameterDecoder;

    private final HandleMethodExecutor handleMethodExecutor;

    private final ExceptionHandling exceptionHandling;

    public RestfulServiceChannelInitializer(final NettyRestfulServiceConfiguration configuration) {
        contextInitializationInboundHandler = new ContextInitializationInboundHandler();
        filterChainInboundHandler = new FilterChainInboundHandler(configuration.getFilterInstances());
        httpRequestDispatcher = new HttpRequestDispatcher(configuration.getControllerInstances(), configuration.isTrailingSlashSensitive());
        handlerParameterDecoder = new HandlerParameterDecoder();
        handleMethodExecutor = new HandleMethodExecutor();
        exceptionHandling = new ExceptionHandling(configuration.getExceptionHandlers());
    }

    @Override
    protected void initChannel(final Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast("contextInitialization", contextInitializationInboundHandler);
        pipeline.addLast("filterChain", filterChainInboundHandler);
        pipeline.addLast("dispatcher", httpRequestDispatcher);
        pipeline.addLast("handlerParameterDecoder", handlerParameterDecoder);
        pipeline.addLast("handleMethodExecutor", handleMethodExecutor);
        pipeline.addLast("exceptionHandling", exceptionHandling);
    }
}
