package org.apache.http.infra.mapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DefaultMappingContext<T> implements MappingContext<T> {

    private final String pattern;

    private final T payload;

    @Override
    public String pattern() {
        return pattern;
    }

    @Override
    public T payload() {
        return payload;
    }
}
