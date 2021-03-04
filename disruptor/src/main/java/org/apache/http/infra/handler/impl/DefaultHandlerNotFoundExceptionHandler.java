package org.apache.http.infra.handler.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.http.infra.consts.Http;
import org.apache.http.infra.handler.ExceptionHandleResult;
import org.apache.http.infra.handler.ExceptionHandler;
import org.apache.http.infra.handler.HandlerNotFoundException;

/**
 * A default handler for {@link HandlerNotFoundException}.
 */
public final class DefaultHandlerNotFoundExceptionHandler implements ExceptionHandler<HandlerNotFoundException> {

    @Override
    public ExceptionHandleResult handleException(final HandlerNotFoundException ex) {
        return ExceptionHandleResult.builder()
                .statusCode(HttpResponseStatus.NOT_FOUND.code())
                .result(ex.getLocalizedMessage())
                .contentType(Http.DEFAULT_CONTENT_TYPE)
                .build();
    }
}
