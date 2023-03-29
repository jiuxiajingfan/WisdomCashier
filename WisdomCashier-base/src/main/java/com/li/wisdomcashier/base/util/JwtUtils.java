package com.li.wisdomcashier.base.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName JWTUtils
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/19 23:11
 * @Version 1.0
 */
@Component
@Slf4j
public class JwtUtils {

    private String secret = "erwrwrwewrws";

    private long expire = 60*60*5;

    private String TOKEN_KEY = "token";



    /**
     * 生成jwt token
     */
    public String generateToken(String userId,String type) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        log.info("{}过期时间为{}",userId,expireDate);
        String token = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .claim("type",type)
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

}
