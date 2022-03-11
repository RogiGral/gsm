package com.gymsystem.gms.filters;

import antlr.Token;
import com.gymsystem.gms.utility.TokenProvider;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.gymsystem.gms.constraints.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {



    private TokenProvider tokenProvider;

    public AuthorizationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
            response.setStatus(OK.value()); // value 200
        } else {
            String authHeader = request.getHeader(AUTHORIZATION);
            if(authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }
            String token = authHeader.substring(TOKEN_PREFIX.length()); //remove prefix
            String username = tokenProvider.getSubject(token);
            if(tokenProvider.isTokenValid(username,token)
                    && SecurityContextHolder.getContext().getAuthentication() == null){
                List<GrantedAuthority> authorityList = tokenProvider.getAuthorities(token);
                Authentication authentication = tokenProvider.getAuthentication(username,authorityList,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);
    }
}
