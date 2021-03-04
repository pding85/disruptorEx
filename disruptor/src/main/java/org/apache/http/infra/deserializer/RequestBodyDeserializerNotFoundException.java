package org.apache.http.infra.deserializer;

import java.text.MessageFormat;

/**
 * {@link RequestBodyDeserializer} not found for specific MIME type.
 */
public final class RequestBodyDeserializerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 828418332240856770L;

    public RequestBodyDeserializerNotFoundException(final String mimeType) {
        super(MessageFormat.format("RequestBodySerializer not found for [{0}]", mimeType));
    }
}