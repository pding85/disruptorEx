package org.apache.http.infra;


import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.infra.filter.FilterChain;

/**
 * HTTP request filter.
 */
public interface Filter {

    /**
     * Do filter.
     *
     * @param httpRequest  HTTP request
     * @param httpResponse HTTP response
     * @param filterChain  filter chain
     */
    void doFilter(FullHttpRequest httpRequest, FullHttpResponse httpResponse, FilterChain filterChain);
}
