package org.apache.http.infra.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.infra.mapping.MappingContext;

@RequiredArgsConstructor
@Getter
@Setter
public final class HandleContext<T> {

    private final FullHttpRequest httpRequest;

    private final FullHttpResponse httpResponse;

    private MappingContext<T> mappingContext;

    private Object[] args = new Object[0];
}

