package org.apache.http.infra.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.infra.handler.HandleContext;
import org.apache.http.infra.handler.Handler;

@Slf4j
@ChannelHandler.Sharable
public final class ContextInitializationInboundHandler extends ChannelInboundHandlerAdapter {

    @SuppressWarnings("NullableProblems")
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        log.debug("{}", msg);
        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.NOT_FOUND, ctx.alloc().buffer());
        HttpUtil.setContentLength(httpResponse, httpResponse.content().readableBytes());
        ctx.fireChannelRead(new HandleContext<Handler>(httpRequest, httpResponse));
    }
}

