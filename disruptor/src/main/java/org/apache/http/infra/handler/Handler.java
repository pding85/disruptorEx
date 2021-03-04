package org.apache.http.infra.handler;

import lombok.Getter;
import org.apache.http.infra.annotation.Param;
import org.apache.http.infra.annotation.ParamSource;
import org.apache.http.infra.annotation.RequestBody;
import org.apache.http.infra.annotation.Returning;
import org.apache.http.infra.consts.Http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class Handler {

    private final Object instance;

    private final Method handleMethod;

    @Getter
    private final List<HandlerParameter> handlerParameters;

    /**
     * HTTP status code to return.
     */
    @Getter
    private final int httpStatusCode;

    /**
     * Content type to producing.
     */
    @Getter
    private final String producing;

    public Handler(final Object instance, final Method handleMethod) {
        this.instance = instance;
        this.handleMethod = handleMethod;
        this.handlerParameters = parseHandleMethodParameter();
        this.httpStatusCode = parseReturning();
        this.producing = parseProducing();
    }

    /**
     * Execute handle method with required arguments.
     *
     * @param args Required arguments
     * @return Method invoke result
     * @throws InvocationTargetException Wraps exception thrown by invoked method
     * @throws IllegalAccessException    Handle method is not accessible
     */
    public Object execute(final Object... args) throws InvocationTargetException, IllegalAccessException {
        return handleMethod.invoke(instance, args);
    }

    private List<HandlerParameter> parseHandleMethodParameter() {
        List<HandlerParameter> params = new LinkedList<>();
        Parameter[] parameters = handleMethod.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param annotation = parameter.getAnnotation(Param.class);
            HandlerParameter handlerParameter;
            RequestBody requestBody;
            if (null != annotation) {
                handlerParameter = new HandlerParameter(i, parameter.getType(), annotation.source(), annotation.name(), annotation.required());
            } else if (null != (requestBody = parameter.getAnnotation(RequestBody.class))) {
                handlerParameter = new HandlerParameter(i, parameter.getType(), ParamSource.BODY, parameter.getName(), requestBody.required());
            } else {
                handlerParameter = new HandlerParameter(i, parameter.getType(), ParamSource.UNKNOWN, parameter.getName(), false);
            }
            params.add(handlerParameter);
        }
        return Collections.unmodifiableList(params);
    }

    private int parseReturning() {
        Returning returning = handleMethod.getAnnotation(Returning.class);
        return Optional.ofNullable(returning).map(Returning::code).orElse(200);
    }

    private String parseProducing() {
        Returning returning = handleMethod.getAnnotation(Returning.class);
        return Optional.ofNullable(returning).map(Returning::contentType).orElse(Http.DEFAULT_CONTENT_TYPE);
    }
}