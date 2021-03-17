package com.pding85.disruptor.cache.util;

import org.apache.ignite.internal.util.typedef.X;
import org.apache.ignite.internal.util.typedef.internal.SB;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

    /** */
    public static final long MB = 1024L * 1024;

    /** */
    public static final long GB = 1024L * 1024 * 1024;

    private static final SimpleDateFormat LONG_DATE_FMT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    /** Debug date format. */
    private static final SimpleDateFormat DEBUG_DATE_FMT = new SimpleDateFormat("HH:mm:ss,SSS");
    private static final byte MASK = 0x0F;

    /**
     * Gets nearest power of 2 larger or equal than v.
     *
     * @param v Value.
     * @return Nearest power of 2.
     */
    public static int ceilPow2(int v) {
        int i = v - 1;

        return Integer.highestOneBit(i) << 1 - (i >>> 30 ^ v >> 31);
    }

    /**
     * @param i Value.
     * @return {@code true} If the given value is power of 2 (0 is not power of 2).
     */
    public static boolean isPow2(int i) {
        return i > 0 && (i & (i - 1)) == 0;
    }


    /**
     * Prints heap usage.
     */
    public static void debugHeapUsage() {
        System.gc();

        Runtime runtime = Runtime.getRuntime();

        X.println('<' + DEBUG_DATE_FMT.format(new Date(System.currentTimeMillis())) + "><DEBUG><" +
                Thread.currentThread().getName() + "> Heap stats [free=" + runtime.freeMemory() / (1024 * 1024) +
                "M, total=" + runtime.totalMemory() / (1024 * 1024) + "M]");
    }

    /**
     * Pretty-formatting for minutes.
     *
     * @param mins Minutes to format.
     * @return Formatted presentation of minutes.
     */
    public static String formatMins(long mins) {
        assert mins >= 0;

        if (mins == 0)
            return "< 1 min";

        SB sb = new SB();

        long dd = mins / 1440; // 1440 mins = 60 mins * 24 hours

        if (dd > 0)
            sb.a(dd).a(dd == 1 ? " day " : " days ");

        mins %= 1440;

        long hh = mins / 60;

        if (hh > 0)
            sb.a(hh).a(hh == 1 ? " hour " : " hours ");

        mins %= 60;

        if (mins > 0)
            sb.a(mins).a(mins == 1 ? " min " : " mins ");

        return sb.toString().trim();
    }
    /**
     *
     * @param m Map to seal.
     * @param <K> Key type.
     * @param <V> Value type
     * @return Sealed map.
     */
    public static <K, V> Map<K, V> sealMap(Map<K, V> m) {
        assert m != null;

        return Collections.unmodifiableMap(new HashMap<>(m));
    }

    /**
     * Construct array with one trust manager which don't reject input certificates.
     *
     * @return Array with one X509TrustManager implementation of trust manager.
     */
    private static TrustManager[] getTrustManagers() {
        return new TrustManager[] {
                new X509TrustManager() {
                    @Nullable
                    @Override public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        /* No-op. */
                    }

                    @Override public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        /* No-op. */
                    }
                }
        };
    }

    /**
     * @param sb String builder.
     * @param b Byte to add in hexadecimal format.
     */
    private static void addByteAsHex(StringBuilder sb, byte b) {
        sb.append(Integer.toHexString(MASK & b >>> 4)).append(Integer.toHexString(MASK & b));
    }

}
