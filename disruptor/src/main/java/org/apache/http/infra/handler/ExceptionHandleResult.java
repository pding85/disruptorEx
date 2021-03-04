package org.apache.http.infra.handler;

import lombok.Builder;
import lombok.Getter;
import org.apache.http.infra.consts.Http;

@Builder
@Getter
public final class ExceptionHandleResult {

    private final Object result;

    @Builder.Default
    private final int statusCode = 500;

    @Builder.Default
    private final String contentType = Http.DEFAULT_CONTENT_TYPE;
}
