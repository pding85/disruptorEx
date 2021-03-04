package org.apache.http.infra.deserializer.factory;

import org.apache.http.infra.deserializer.RequestBodyDeserializer;

public interface DeserializerFactory {

    /**
     * Specify which type would be deserialized by the deserializer created by this factory.
     *
     * @return MIME type
     */
    String mimeType();

    /**
     * Deserializer factory method.
     *
     * @return Instance of deserializer
     */
    RequestBodyDeserializer createDeserializer();
}

