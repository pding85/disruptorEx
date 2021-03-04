package org.apache.http.infra.handler.impl;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.http.infra.consts.Http;
import org.apache.http.infra.handler.ExceptionHandleResult;
import org.apache.http.infra.handler.ExceptionHandler;

/**
 * A default handler for handling {@link Throwable}.
 */
public final class DefaultExceptionHandler implements ExceptionHandler<Throwable> {

    @Override
    public ExceptionHandleResult handleException(final Throwable ex) {
        return ExceptionHandleResult.builder()
                .statusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .contentType(Http.DEFAULT_CONTENT_TYPE)
                .result(ex.getLocalizedMessage())
                .build();
    }
}
