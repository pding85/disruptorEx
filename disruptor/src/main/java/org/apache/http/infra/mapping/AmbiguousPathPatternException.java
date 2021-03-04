package org.apache.http.infra.mapping;

public final class AmbiguousPathPatternException extends RuntimeException {

    private static final long serialVersionUID = -7109813692538597236L;

    public AmbiguousPathPatternException(final String message) {
        super(message);
    }
}
