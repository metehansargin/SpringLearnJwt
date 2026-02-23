package com.metehansargin.jwt.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            String header;
            String token;
            String userName;

            header=request.getHeader("Authorization");
            if (header==null){
                filterChain.doFilter(request,response);
                return;
            }
            token=header.substring(7);
            try{
                userName=jwtService.getUser(token);
                if (userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
                    if (userDetails!=null&&jwtService.getExpiration(token)){
                        UsernamePasswordAuthenticationToken authenticationToken=
                                new UsernamePasswordAuthenticationToken(
                                userName,null,userDetails.getAuthorities());
                        authenticationToken.setDetails(userDetails);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }catch (ExpiredJwtException e){
                System.out.println("Token Expired..."+e.getMessage());
            }catch (Exception e){
                System.out.println("General one exception ..."+e.getMessage());
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request,response);
    }
}
