package org.apache.http.mapping;

import java.util.Map;

/**
 * PathMatcher is a supporting tool for HTTP request dispatching.
 * <p>
 * Used by {@link UrlPatternMap},
 * for template variables extracting, path pattern validating, pattern matching.
 * </p>
 *
 * @see RegexPathMatcher
 */
public interface PathMatcher {

    /**
     * Capture actual values of placeholder.
     * The format of Path pattern likes <code>/app/{jobName}/{status}</code>.
     *
     * @param pathPattern path pattern contains templates
     * @param path actual path
     * @return map from template name to actual value
     */
    Map<String, String> captureVariables(String pathPattern, String path);

    /**
     * Check if the path pattern matches the given path.
     *
     * @param pathPattern path pattern
     * @param path the path to check
     * @return true if matched, or else false
     */
    boolean matches(String pathPattern, String path);

    /**
     * Check if the given string is a valid path pattern.
     *
     * @param pathPattern path pattern to check
     * @return true if valid, or else false
     */
    boolean isValidPathPattern(String pathPattern);
}