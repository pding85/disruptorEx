package org.apache.http.infra.handler;

public interface ExceptionHandler<E extends Throwable> {

    /**
     * Handler for specific Exception.
     *
     * @param ex Exception
     * @return Handle result
     */
    ExceptionHandleResult handleException(E ex);
}