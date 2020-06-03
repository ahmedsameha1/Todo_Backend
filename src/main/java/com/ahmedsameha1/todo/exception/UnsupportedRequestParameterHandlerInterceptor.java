package com.ahmedsameha1.todo.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UnsupportedRequestParameterHandlerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            List<String> supportedParameters = Stream.of(handlerMethod.getMethodParameters())
                    .flatMap(methodParameter ->
                        Stream.of(methodParameter.getParameterAnnotation(RequestParam.class)))
                                .filter(Objects::nonNull).map(RequestParam::name)
                                .collect(Collectors.toList());
            List<String> parameters = Collections.list(request.getParameterNames());
            parameters.removeAll(supportedParameters);
            List<String> unsupportedParameters = parameters;
            if (!unsupportedParameters.isEmpty()) {
                throw new UnsupportedRequestParameterException(unsupportedParameters);
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
