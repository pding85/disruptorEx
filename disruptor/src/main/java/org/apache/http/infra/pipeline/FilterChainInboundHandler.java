package org.apache.http.infra.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.infra.Filter;
import org.apache.http.infra.filter.DefaultFilterChain;
import org.apache.http.infra.filter.FilterChain;
import org.apache.http.infra.handler.HandleContext;
import org.apache.http.infra.handler.Handler;

import java.util.List;

/**
 * Filter chain inbound handler.
 */
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public final class FilterChainInboundHandler extends ChannelInboundHandlerAdapter {

    private final List<Filter> filterInstances;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        if (filterInstances.isEmpty()) {
            ctx.fireChannelRead(msg);
            return;
        }
        HandleContext<Handler> handleContext = (HandleContext<Handler>) msg;
        FilterChain filterChain = new DefaultFilterChain(filterInstances, ctx, handleContext);
        filterChain.next(handleContext.getHttpRequest());
    }
}
