package org.apache.http.infra.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.infra.annotation.ParamSource;

/**
 * Describe parameters of a handle method.
 */
@RequiredArgsConstructor
@Getter
public final class HandlerParameter {

    private final int index;

    private final Class<?> type;

    private final ParamSource paramSource;

    private final String name;

    private final boolean required;
}