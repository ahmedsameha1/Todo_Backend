package com.ahmedsameha1.todo.security.filter;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;

import static com.ahmedsameha1.todo.security.Constants.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private String secretKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secretKey) {
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
    }

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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String jws = Jwts.builder()
                .setSubject(((UserDetails) authResult.getPrincipal()).getUsername())
                .setExpiration(Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC()).plusDays(JWT_TOKEN_EXPIRATION_PERIOD_IN_DAYS)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))).compact();
        response.addHeader(AUTHORIZATION, TOKEN_PREFIX + jws);
    }
}
