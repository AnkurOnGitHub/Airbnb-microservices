package com.airbnb.api_gateway.security;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String USER_ID_HEADER = "X-User-Id";
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userId = null;
        if(header != null && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null){
            String token = header.substring(7).trim();
            try {
                Claims claims = jwtService.parseAccessToken(token);
                userId = claims.getSubject();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
                authenticationToken.setDetails(claims);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }catch (Exception e){
                SecurityContextHolder.clearContext();
            }
        }

        String finalUserId = userId;
        filterChain.doFilter(new HttpServletRequestWrapper(request){
            @Override
            public  String getHeader(String name){
                if(USER_ID_HEADER.equalsIgnoreCase(name)){
                    return finalUserId;
                }
                return super.getHeader(name);
            }
            @Override
            public Enumeration<String>getHeaders(String name){
                if(USER_ID_HEADER.equalsIgnoreCase(name))
                    return Collections.enumeration(List.of(finalUserId));
                return super.getHeaders(name);
            }
            @Override
            public Enumeration<String>getHeaderNames(){
                List<String> names = Collections.list(super.getHeaderNames());
                if (names.stream().noneMatch(USER_ID_HEADER::equalsIgnoreCase))
                    names.add(USER_ID_HEADER);
                return Collections.enumeration(names);
            }
        },response);


    }
}
