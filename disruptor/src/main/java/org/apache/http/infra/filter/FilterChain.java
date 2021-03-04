package org.apache.http.infra.filter;


import io.netty.handler.codec.http.FullHttpRequest;

/**

 */
public interface FilterChain {

    /**
     * Next filter.
     *
     * @param httpRequest HTTP request
     */
    void next(FullHttpRequest httpRequest);
}