package org.apache.http.infra.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.infra.RestfulController;
import org.apache.http.infra.annotation.ContextPath;
import org.apache.http.infra.annotation.Mapping;
import org.apache.http.infra.handler.HandleContext;
import org.apache.http.infra.handler.Handler;
import org.apache.http.infra.handler.HandlerMappingRegistry;
import org.apache.http.infra.handler.HandlerNotFoundException;
import org.apache.http.infra.mapping.MappingContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@ChannelHandler.Sharable
public final class HttpRequestDispatcher extends ChannelInboundHandlerAdapter {

    private static final String TRAILING_SLASH = "/";

    private final HandlerMappingRegistry mappingRegistry = new HandlerMappingRegistry();

    private final boolean trailingSlashSensitive;

    public HttpRequestDispatcher(final List<RestfulController> restfulControllers, final boolean trailingSlashSensitive) {
        this.trailingSlashSensitive = trailingSlashSensitive;
        initMappingRegistry(restfulControllers);
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        HandleContext<Handler> handleContext = (HandleContext<Handler>) msg;
        FullHttpRequest request = handleContext.getHttpRequest();
        if (!trailingSlashSensitive) {
            request.setUri(appendTrailingSlashIfAbsent(request.uri()));
        }
        Optional<MappingContext<Handler>> mappingContext = mappingRegistry.getMappingContext(request);
        if (mappingContext.isPresent()) {
            handleContext.setMappingContext(mappingContext.get());
            ctx.fireChannelRead(handleContext);
        } else {
            ReferenceCountUtil.release(request);
            throw new HandlerNotFoundException(request.uri());
        }
    }

    private void initMappingRegistry(final List<RestfulController> restfulControllers) {
        for (RestfulController restfulController : restfulControllers) {
            Class<? extends RestfulController> controllerClass = restfulController.getClass();
            String contextPath = Optional.ofNullable(controllerClass.getAnnotation(ContextPath.class)).map(ContextPath::value).orElse("");
            for (Method method : controllerClass.getMethods()) {
                Mapping mapping = method.getAnnotation(Mapping.class);
                if (null == mapping) {
                    continue;
                }
                HttpMethod httpMethod = HttpMethod.valueOf(mapping.method());
                String path = mapping.path();
                String fullPathPattern = resolveFullPath(contextPath, path);
                if (!trailingSlashSensitive) {
                    fullPathPattern = appendTrailingSlashIfAbsent(fullPathPattern);
                }
                mappingRegistry.addMapping(httpMethod, fullPathPattern, new Handler(restfulController, method));
            }
        }
    }

    private String resolveFullPath(final String contextPath, final String pattern) {
        return Optional.ofNullable(contextPath).orElse("") + pattern;
    }

    private String appendTrailingSlashIfAbsent(final String uri) {
        String[] split = uri.split("\\?");
        if (1 == split.length) {
            return uri.endsWith(TRAILING_SLASH) ? uri : uri + TRAILING_SLASH;
        }
        String path = split[0];
        return path.endsWith(TRAILING_SLASH) ? uri : path + TRAILING_SLASH + "?" + split[1];
    }
}
