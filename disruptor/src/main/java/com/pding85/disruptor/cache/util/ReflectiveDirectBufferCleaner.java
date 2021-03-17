package com.pding85.disruptor.cache.util;

import org.apache.ignite.internal.util.DirectBufferCleaner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * {@link org.apache.ignite.internal.util.DirectBufferCleaner} implementation based on {@code sun.misc.Cleaner} and
 * {@code sun.nio.ch.DirectBuffer.cleaner()} method.
 *
 * Mote: This implementation will not work on Java 9+.
 */
public class ReflectiveDirectBufferCleaner implements DirectBufferCleaner {
    /** Cleaner method. */
    private final Method cleanerMtd;

    /** Clean method. */
    private final Method cleanMtd;

    /** */
    public ReflectiveDirectBufferCleaner() {
        try {
            cleanerMtd = Class.forName("sun.nio.ch.DirectBuffer").getMethod("cleaner");

        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException("No sun.nio.ch.DirectBuffer.cleaner() method found", e);
        }

        try {
            cleanMtd = Class.forName("sun.misc.Cleaner").getMethod("clean");
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException("No sun.misc.Cleaner.clean() method found", e);
        }
    }

    /** {@inheritDoc} */
    @Override public void clean(ByteBuffer buf) {
        try {
            cleanMtd.invoke(cleanerMtd.invoke(buf));
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke direct buffer cleaner", e);
        }
    }
}