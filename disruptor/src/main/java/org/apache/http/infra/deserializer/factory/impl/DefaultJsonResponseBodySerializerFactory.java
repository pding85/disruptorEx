package org.apache.http.infra.deserializer.factory.impl;

import io.netty.handler.codec.http.HttpHeaderValues;
import org.apache.http.infra.deserializer.ResponseBodySerializer;
import org.apache.http.infra.deserializer.factory.SerializerFactory;
import org.apache.http.infra.deserializer.impl.DefaultJsonResponseBodySerializer;

public final class DefaultJsonResponseBodySerializerFactory implements SerializerFactory {

    @Override
    public String mimeType() {
        return HttpHeaderValues.APPLICATION_JSON.toString();
    }

    @Override
    public ResponseBodySerializer createSerializer() {
        return new DefaultJsonResponseBodySerializer();
    }
}
