package com.pding85.disruptor;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class Util {

    private static final Unsafe THE_UNSAFE;
    static
    {
        try
        {
            final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>()
            {
                public Unsafe run() throws Exception
                {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            };

            THE_UNSAFE = AccessController.doPrivileged(action);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    /**
     * Get a handle on the Unsafe instance, used for accessing low-level concurrency
     * and memory constructs.
     * @return The Unsafe
     */
    public static Unsafe getUnsafe()
    {
        return THE_UNSAFE;
    }

    /**
     * Gets the address value for the memory that backs a direct byte buffer.
     * @param buffer
     * @return The system address for the buffers
     */
    public static long getAddressFromDirectByteBuffer(ByteBuffer buffer)
    {
        try
        {
            Field addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            return addressField.getLong(buffer);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to address field from ByteBuffer", e);
        }
    }


    /**
     * Calculate the log base 2 of the supplied integer, essentially reports the location
     * of the highest bit.
     *
     * @param i Value to calculate log2 for.
     * @return The log2 value
     */
    public static int log2(int i)
    {
        int r = 0;
        while ((i >>= 1) != 0)
        {
            ++r;
        }
        return r;
    }

    public static void main(String[] args) {
        final int BUFFER_PAD;
        final long REF_ARRAY_BASE;
        final int REF_ELEMENT_SHIFT;

        Unsafe UNSAFE = com.lmax.disruptor.util.Util.getUnsafe();
        final int scale = UNSAFE.arrayIndexScale(Object[].class);
        System.out.println(scale);

        if (4 == scale)
        {
            REF_ELEMENT_SHIFT = 2;
        }
        else if (8 == scale)
        {
            REF_ELEMENT_SHIFT = 3;
        }
        else
        {
            throw new IllegalStateException("Unknown pointer size");
        }
        BUFFER_PAD = 128 / scale;
        // Including the buffer pad in the array base offset
        REF_ARRAY_BASE = UNSAFE.arrayBaseOffset(Object[].class) + (BUFFER_PAD << REF_ELEMENT_SHIFT);
        System.out.println(REF_ARRAY_BASE);
    }
}
