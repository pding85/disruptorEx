package org.apache.http.infra.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import java.lang.reflect.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Gson factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GsonFactory {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    private static volatile Gson gson = GSON_BUILDER.create();

    private static final JsonParser JSON_PARSER = new JsonParser();

    /**
     * Register type adapter.
     *
     * @param type Gson type
     * @param typeAdapter Gson type adapter
     */
    public static synchronized void registerTypeAdapter(final Type type, final TypeAdapter typeAdapter) {
        GSON_BUILDER.registerTypeAdapter(type, typeAdapter);
        gson = GSON_BUILDER.create();
    }

    /**
     * Get gson instance.
     *
     * @return gson instance
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * Get json parser.
     *
     * @return json parser instance
     */
    public static JsonParser getJsonParser() {
        return JSON_PARSER;
    }
}
