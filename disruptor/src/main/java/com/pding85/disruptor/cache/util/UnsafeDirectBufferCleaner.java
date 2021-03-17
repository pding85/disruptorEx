package com.pding85.disruptor.cache.util;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import org.apache.ignite.internal.util.DirectBufferCleaner;
import org.apache.ignite.internal.util.GridUnsafe;
import sun.misc.Unsafe;

/**
 * {@link org.apache.ignite.internal.util.DirectBufferCleaner} implementation based on {@code Unsafe.invokeCleaner} method.
 *
 * Note: This implementation will work only for Java 9+.
 */
public class UnsafeDirectBufferCleaner implements DirectBufferCleaner {
    /** Cleaner method. */
    private final Method cleanerMtd;

    /** */
    public UnsafeDirectBufferCleaner() {
        try {
            cleanerMtd = Unsafe.class.getMethod("invokeCleaner", ByteBuffer.class);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("Reflection failure: no sun.misc.Unsafe.invokeCleaner() method found", e);
        }
    }

    /** {@inheritDoc} */
    @Override public void clean(ByteBuffer buf) {
        GridUnsafe.invoke(cleanerMtd, buf);
    }
}