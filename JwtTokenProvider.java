package com.example.msa_monolihic.member.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;  //주의
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretkey;

    @Value("${jwt.expirationRt}")
    private int expirationRt;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    private Key ENCTYPT_SECRET_KEY;
    private Key ENCTYPT_RT_SECRET_KEY;

    // 생성자가 호출되고, 스프링빈이 만들어진 직후에 아래 메서드 바로 실행하는 어노테이션
    @PostConstruct
    public void init(){
        ENCTYPT_SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretkey)
                                                , SignatureAlgorithm.HS512.getJcaName());
        ENCTYPT_RT_SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKeyRt)
                                                , SignatureAlgorithm.HS512.getJcaName());
    }
    //AccessToken
    //claims는 사용자정보(페이로드 정보)
    public String createToken(String id, String role){
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role",role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration*60*1000L))
                .signWith(ENCTYPT_SECRET_KEY)
                .compact();
        return token;
    }

    //AccessToken
    //claims는 사용자정보(페이로드 정보)
    public String createRefreshToken(String id, String role){
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role",role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationRt*60*1000L))
                .signWith(ENCTYPT_RT_SECRET_KEY)
                .compact();
        return token;
    }

}
