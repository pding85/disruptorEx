package org.apache.http.infra.deserializer;

/**
 * Serializer for serializing response body with specific MIME type.
 */
public interface ResponseBodySerializer {

    /**
     * Specify which type would be serialized by this serializer.
     *
     * @return MIME type
     */
    String mimeType();

    /**
     * Serialize object to bytes.
     *
     * @param responseBody object to be serialized
     * @return bytes
     */
    byte[] serialize(Object responseBody);
}
