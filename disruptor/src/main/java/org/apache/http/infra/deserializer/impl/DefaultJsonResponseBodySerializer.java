package org.apache.http.infra.deserializer.impl;

import com.google.gson.Gson;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.apache.http.infra.deserializer.ResponseBodySerializer;
import org.apache.http.infra.json.GsonFactory;

import java.nio.charset.StandardCharsets;

/**
 * Default serializer for <code>application/json</code>.
 */
public final class DefaultJsonResponseBodySerializer implements ResponseBodySerializer {

    private final Gson gson = GsonFactory.getGson();

    @Override
    public String mimeType() {
        return HttpHeaderValues.APPLICATION_JSON.toString();
    }

    @Override
    public byte[] serialize(final Object responseBody) {
        if (responseBody instanceof String) {
            return ((String) responseBody).getBytes(StandardCharsets.UTF_8);
        }
        return gson.toJson(responseBody).getBytes(StandardCharsets.UTF_8);
    }
}