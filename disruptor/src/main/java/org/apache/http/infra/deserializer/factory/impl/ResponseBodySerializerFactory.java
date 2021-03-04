package org.apache.http.infra.deserializer.factory.impl;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.infra.deserializer.ResponseBodySerializer;
import org.apache.http.infra.deserializer.ResponseBodySerializerNotFoundException;
import org.apache.http.infra.deserializer.factory.SerializerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Response body serializer factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseBodySerializerFactory {

    private static final Map<String, ResponseBodySerializer> RESPONSE_BODY_SERIALIZERS = new ConcurrentHashMap<>();

    private static final Map<String, SerializerFactory> RESPONSE_BODY_SERIALIZER_FACTORIES = new ConcurrentHashMap<>();

    private static final ResponseBodySerializer MISSING_SERIALIZER = new ResponseBodySerializer() {
        @Override
        public String mimeType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public byte[] serialize(final Object responseBody) {
            throw new UnsupportedOperationException();
        }
    };

    static {
        for (ResponseBodySerializer serializer : ServiceLoader.load(ResponseBodySerializer.class)) {
            RESPONSE_BODY_SERIALIZERS.put(serializer.mimeType(), serializer);
        }
        for (SerializerFactory factory : ServiceLoader.load(SerializerFactory.class)) {
            RESPONSE_BODY_SERIALIZER_FACTORIES.put(factory.mimeType(), factory);
        }
    }

    /**
     * Get serializer for specific HTTP content type.
     *
     * <p>
     * This method will look for a serializer instance of specific MIME type.
     * If serializer not found, this method would look for serializer factory by MIME type.
     * If it is still not found, the MIME type would be marked as <code>MISSING_SERIALIZER</code>.
     * </p>
     *
     * <p>
     * Some default serializer will be provided by {@link SerializerFactory},
     * so developers can implement {@link ResponseBodySerializer} and register it by SPI to override default serializer.
     * </p>
     *
     * @param contentType HTTP content type
     * @return serializer
     */
    public static ResponseBodySerializer getResponseBodySerializer(final String contentType) {
        ResponseBodySerializer result = RESPONSE_BODY_SERIALIZERS.get(contentType);
        if (null == result) {
            synchronized (ResponseBodySerializerFactory.class) {
                if (null == RESPONSE_BODY_SERIALIZERS.get(contentType)) {
                    instantiateResponseBodySerializerFromFactories(contentType);
                }
                result = RESPONSE_BODY_SERIALIZERS.get(contentType);
            }
        }
        if (MISSING_SERIALIZER == result) {
            throw new ResponseBodySerializerNotFoundException(contentType);
        }
        return result;
    }

    private static void instantiateResponseBodySerializerFromFactories(final String contentType) {
        ResponseBodySerializer serializer;
        SerializerFactory factory = RESPONSE_BODY_SERIALIZER_FACTORIES.get(contentType);
        serializer = Optional.ofNullable(factory).map(SerializerFactory::createSerializer).orElse(MISSING_SERIALIZER);
        RESPONSE_BODY_SERIALIZERS.put(contentType, serializer);
    }
}
