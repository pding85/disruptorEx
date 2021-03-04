package org.apache.http.infra.deserializer.factory.impl;

import io.netty.handler.codec.http.HttpHeaderValues;
import org.apache.http.infra.deserializer.RequestBodyDeserializer;
import org.apache.http.infra.deserializer.factory.DeserializerFactory;
import org.apache.http.infra.deserializer.impl.DefaultTextPlainRequestBodyDeserializer;

public final class DefaultTextPlainRequestBodyDeserializerFactory implements DeserializerFactory {

    @Override
    public String mimeType() {
        return HttpHeaderValues.TEXT_PLAIN.toString();
    }

    @Override
    public RequestBodyDeserializer createDeserializer() {
        return new DefaultTextPlainRequestBodyDeserializer();
    }
}
