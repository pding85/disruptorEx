package org.apache.http.infra.deserializer.impl;

import com.google.gson.Gson;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.apache.http.infra.deserializer.RequestBodyDeserializer;
import org.apache.http.infra.json.GsonFactory;

import java.nio.charset.StandardCharsets;

public final class DefaultJsonRequestBodyDeserializer implements RequestBodyDeserializer {

    private final Gson gson = GsonFactory.getGson();

    @Override
    public String mimeType() {
        return HttpHeaderValues.APPLICATION_JSON.toString();
    }

    @Override
    public <T> T deserialize(final Class<T> targetType, final byte[] requestBodyBytes) {
        if (0 == requestBodyBytes.length) {
            return null;
        }
        return gson.fromJson(new String(requestBodyBytes, StandardCharsets.UTF_8), targetType);
    }
}

