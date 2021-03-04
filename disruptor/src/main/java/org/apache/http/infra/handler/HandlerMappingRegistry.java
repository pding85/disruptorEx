package org.apache.http.infra.handler;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.http.infra.mapping.MappingContext;
import org.apache.http.infra.mapping.RegexUrlPatternMap;
import org.apache.http.infra.mapping.UrlPatternMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * HandlerMappingRegistry stores mappings of handlers.
 * Search a proper   by HTTP method and request URI.
 */
public final class HandlerMappingRegistry {

    private final Map<HttpMethod, UrlPatternMap<Handler>> mappings = new HashMap<>();

    /**
     * Get a MappingContext with Handler for the request.
     *
     * @param httpRequest HTTP request
     * @return A MappingContext if matched, return null if mismatched.
     */
    public Optional<MappingContext<Handler>> getMappingContext(final HttpRequest httpRequest) {
        String uriWithoutQuery = httpRequest.uri().split("\\?")[0];
        return Optional.ofNullable(mappings.get(httpRequest.method())).map(urlPatternMap -> urlPatternMap.match(uriWithoutQuery));
    }

    /**
     * Add a Handler for a path pattern.
     *
     * @param method HTTP method
     * @param pathPattern path pattern
     * @param handler handler
     */
    public void addMapping(final HttpMethod method, final String pathPattern, final Handler handler) {
        UrlPatternMap<Handler> urlPatternMap = mappings.computeIfAbsent(method, httpMethod -> new RegexUrlPatternMap<>());
        urlPatternMap.put(pathPattern, handler);
    }
}
