package com.mrn.mobileappws.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // access the request header - Authorization header (see postman)
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        // header is null or does not start with Bearer prefix (very important!)
        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);


    }

    // return user password authentication token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(SecurityConstants.TOKEN_PREFIX);

        if(token != null) {
            // replace the prefix with empty replacement string
            // we don't need the bearer,  we need to get the token from the http request
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

            String user = Jwts.parser()
                              .setSigningKey(SecurityConstants.getTokenSecret()) // without this secret key we cannot parse the token
                              .parseClaimsJws(token)
                              .getBody()
                              .getSubject();

            if(user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList());
            }
            return null;
        }

        return null;
    }
}
