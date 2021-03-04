package org.apache.http.infra;


import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.infra.handler.ExceptionHandler;

import java.util.*;

/**
 * Configuration for {@link NettyRestfulService}.
 */
@Getter
@RequiredArgsConstructor
public final class NettyRestfulServiceConfiguration {

    private final int port;

    @Setter
    private String host;

    /**
     * If trailing slash sensitive, <code>/foo/bar</code> is not equals to <code>/foo/bar/</code>.
     */
    @Setter
    private boolean trailingSlashSensitive;

    private final List<Filter> filterInstances = new LinkedList<>();

    private final List<RestfulController> controllerInstances = new LinkedList<>();

    private final Map<Class<? extends Throwable>, ExceptionHandler<? extends Throwable>> exceptionHandlers = new HashMap<>();

    /**
     * Add instances of {@link Filter}.
     *
     * @param instances instances of Filter
     */
    public void addFilterInstances(final Filter... instances) {
        filterInstances.addAll(Arrays.asList(instances));
    }

    /**
     * Add instances of RestfulController.
     *
     * @param instances instances of RestfulController
     */
    public void addControllerInstances(final RestfulController... instances) {
        controllerInstances.addAll(Arrays.asList(instances));
    }

    /**
     * Add an instance of ExceptionHandler for specific exception.
     *
     * @param exceptionType    The type of exception to handle
     * @param exceptionHandler Instance of ExceptionHandler
     * @param <E>              The type of exception to handle
     */
    public <E extends Throwable> void addExceptionHandler(final Class<E> exceptionType, final ExceptionHandler<E> exceptionHandler) {
        Preconditions.checkState(!exceptionHandlers.containsKey(exceptionType), "ExceptionHandler for %s has already existed.", exceptionType.getName());
        exceptionHandlers.put(exceptionType, exceptionHandler);
    }
}
