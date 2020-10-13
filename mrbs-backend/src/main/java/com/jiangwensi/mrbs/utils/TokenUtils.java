package com.jiangwensi.mrbs.utils;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.constant.PropKeyConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Component
public class TokenUtils {

    static Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    public static String generateToken(String subject, long validity) {
        Date expiry = new Date(new Date().getTime() + validity);

        String secretKey = AppProperties.getAppProperties().getProperty(PropKeyConst.SECRETKEY);
        JwtBuilder jwtBuilder =
                Jwts.builder()
                        .setSubject(subject)
                        .setExpiration(expiry)
                        .signWith(SignatureAlgorithm.HS512, secretKey);

        String token = jwtBuilder.compact();
        logger.debug("generatedToken:"+token);

        return token;
    }

    public static Claims decodeToken(String token) {
        String secretkey = AppProperties.getAppProperties().getProperty(PropKeyConst.SECRETKEY);

        return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
    }

    //decodeToken will throw io.jsonwebtoken.ExpiredJwtException if token is expired
    public static boolean isExpired(String token){
        return decodeToken(token).getExpiration().before(new Date());
    }

    public static String retrieveSubject(String token){
        return decodeToken(token).getSubject();
    }

}
