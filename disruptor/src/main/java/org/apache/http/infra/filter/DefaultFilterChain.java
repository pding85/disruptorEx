package org.apache.http.infra.filter;


import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.infra.Filter;
import org.apache.http.infra.handler.HandleContext;

import java.util.List;

/**
 * Default filter chain.
 */
public final class DefaultFilterChain implements FilterChain {

    private final Filter[] filters;

    private final ChannelHandlerContext ctx;

    private final HandleContext<?> handleContext;

    private int current;

    private boolean passedThrough;

    private boolean replied;

    public DefaultFilterChain(final List<Filter> filterInstances, final ChannelHandlerContext ctx, final HandleContext<?> handleContext) {
        filters = filterInstances.toArray(new Filter[0]);
        this.ctx = ctx;
        this.handleContext = handleContext;
    }

    @Override
    public void next(final FullHttpRequest httpRequest) {
        Preconditions.checkState(!passedThrough && !replied, "FilterChain has already finished.");
        if (current < filters.length) {
            filters[current++].doFilter(httpRequest, handleContext.getHttpResponse(), this);
            if (!passedThrough && !replied) {
                doResponse();
            }
            return;
        }
        passedThrough = true;
        ctx.fireChannelRead(handleContext);
    }

    private void doResponse() {
        try {
            ctx.writeAndFlush(handleContext.getHttpResponse());
            replied = true;
        } finally {
            ReferenceCountUtil.release(handleContext.getHttpRequest());
        }
    }
}
