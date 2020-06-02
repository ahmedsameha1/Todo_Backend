package com.ahmedsameha1.todo.exception;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

//This class isn't needed
//I Created it for just for completeness
//The configuration of HttpSecurity in WebSecurityConfigurer will respond with 403 for not allowed whitelist methods
@Component
public class HttpMethodsFilter extends OncePerRequestFilter {
    private static final Set<HttpMethod> allowedHttpMethods
            = Set.of(HttpMethod.GET, HttpMethod.POST,
            HttpMethod.PUT, HttpMethod.DELETE);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        if (allowedHttpMethods.stream().noneMatch(httpMethod -> httpMethod.matches(method))) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}