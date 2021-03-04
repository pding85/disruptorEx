package org.apache.http.infra.deserializer;

import java.text.MessageFormat;

/**
 * {@link ResponseBodySerializer} not found for specific MIME type.
 */
public final class ResponseBodySerializerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3201288074956273247L;

    public ResponseBodySerializerNotFoundException(final String mimeType) {
        super(MessageFormat.format("ResponseBodySerializer not found for [{0}]", mimeType));
    }
}
