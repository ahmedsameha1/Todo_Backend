package com.ahmedsameha1.todo.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
@Aspect
public class HandleRequestRejectedExceptionNonWhitelist {
    @Around("execution(public void org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public void handle(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            proceedingJoinPoint.proceed();
        } catch (RequestRejectedException exception) {
            if (exception.getMessage().contains("whitelist")) {
                HttpServletResponse  httpServletResponse = (HttpServletResponse) proceedingJoinPoint.getArgs()[1];
                httpServletResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        }
    }
}