package org.apache.http.infra.deserializer.factory;

import org.apache.http.infra.deserializer.ResponseBodySerializer;

public interface SerializerFactory {

    /**
     * Specify which type would be serialized by the serializer created by this factory.
     *
     * @return MIME type
     */
    String mimeType();

    /**
     * Serializer factory method.
     *
     * @return instance of serializer
     */
    ResponseBodySerializer createSerializer();
}
