package com.danghieu99.monolith.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

import jakarta.servlet.http.Cookie;

import java.util.Date;

public class TokenUtil {
    public static String buildToken(SecretKey secretKey, Claims claims) {
        return Jwts.builder()
                .subject(claims.getSubject())
                .issuer(claims.getIssuer())
                .issuedAt(new Date(System.currentTimeMillis()))
                .claims(claims)
                .expiration(claims.getExpiration())
                .signWith(secretKey)
                .compact();
    }

    public static Claims parseClaimsFromToken(SecretKey secretKey, String issuer, String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(issuer)
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }


    public static String parseTokenFromCookies(Cookie[] cookies, String tokenName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equals(tokenName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}