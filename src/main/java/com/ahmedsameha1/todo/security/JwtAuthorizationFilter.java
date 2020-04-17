package com.ahmedsameha1.todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.ahmedsameha1.todo.security.Constants.AUTHORIZATION;
import static com.ahmedsameha1.todo.security.Constants.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.token.secret}")
    private String secretKey;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
           try {
               String jwt = authorizationHeader.replace(TOKEN_PREFIX,"");
               Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build()
                       .parseClaimsJws(jwt);
               Claims claims = claimsJws.getBody();
               LocalDateTime expiration = claims.getExpiration().toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
               if (LocalDateTime.now(ZoneOffset.UTC).isBefore(expiration)) {
                   String username = claims.getSubject();
                   if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                               new UsernamePasswordAuthenticationToken(userDetailsService
                               .loadUserByUsername(username), null);
                       usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                       SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                       chain.doFilter(request, response);
                   } else {
                       super.doFilterInternal(request, response, chain);
                   }
               } else {
                   super.doFilterInternal(request, response, chain);
               }
           } catch (JwtException e) {
               super.doFilterInternal(request, response, chain);
           }
        } else {
            super.doFilterInternal(request, response, chain);
        }
    }

}
