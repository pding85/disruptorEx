package com.pding85.disruptor.cache.util;

import java.nio.ByteBuffer;

/**
 * Cleaner interface for {@code java.nio.ByteBuffer}.
 */
public interface DirectBufferCleaner {
    /**
     * Cleans direct buffer.
     *
     * @param buf direct buffer.
     */
    public void clean(ByteBuffer buf);
}
