package com.metehansargin.jwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    public static final String SECRET_KEY="PACysyJMV06byXmKLWELgfJO9i1/rEVIf2aK9SpsxZE=";

    public String generateToken(UserDetails userDetails){
       return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*30))
                .signWith(getKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    public Key getKey(){
        byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T exportToken(String token, Function<Claims,T> claimsTFunction){
        Claims claims=Jwts
                .parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody();

        return claimsTFunction.apply(claims);
    }
    public String getUser(String token){
        return exportToken(token,Claims::getSubject);
    }
    public boolean getExpiration(String token){
        Date expiredDate= exportToken(token,Claims::getExpiration);
        return new Date().before(expiredDate);
    }
}
