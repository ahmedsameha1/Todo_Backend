package com.ahmedsameha1.todo.security.filter;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {

           UserAccount userAccount = new ObjectMapper().readValue(request.getInputStream(), UserAccount.class);
           return authenticationManager
                   .authenticate(new UsernamePasswordAuthenticationToken(userAccount.getUsername(), userAccount.getPassword()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
