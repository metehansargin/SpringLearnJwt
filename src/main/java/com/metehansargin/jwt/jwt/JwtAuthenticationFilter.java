package com.metehansargin.jwt.jwt;


import com.metehansargin.jwt.exception.BaseException;
import com.metehansargin.jwt.exception.ErrorMessage;
import com.metehansargin.jwt.exception.MessageType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

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
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = """
        {
            "status": 401,
            "error": "TOKEN_EXPIRED",
            "message": "Token süresi doldu"
        }
        """;

                response.getWriter().write(json);
                response.getWriter().flush();
                return;
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String json = """
        {
            "status": 401,
            "error": "GENERAL_EXCEPTION",
            "message": "Geçersiz token"
        }
        """;

                response.getWriter().write(json);
                response.getWriter().flush();
                return;
            }
            filterChain.doFilter(request,response);
    }
}
