package org.apache.http.infra.mapping;

public interface MappingContext<T> {

    /**
     * The path pattern of mapping context.
     *
     * @return path pattern
     */
    String pattern();

    /**
     * Payload of mapping context.
     *
     * @return payload
     */
    T payload();
}
