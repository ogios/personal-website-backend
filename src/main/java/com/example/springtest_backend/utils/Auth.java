package com.example.springtest_backend.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.springtest_backend.entity.TokenSubject;
import com.example.springtest_backend.entity.User;
import com.example.springtest_backend.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class Auth {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static Auth auth;

    @PostConstruct
    public void init(){
        auth = this;
        auth.userMapper = this.userMapper;
        auth.objectMapper = this.objectMapper;
        auth.EXPIRE_SEC = 3600 * 24 * this.EXPIRE_SEC;
        auth.secret = this.secret;
        System.out.println("secret = " + secret);
        System.out.println("EXPIRE_SEC = " + EXPIRE_SEC);
    }

    @Value("${auth.expire-days}")
    private int EXPIRE_SEC;

    @Value("${auth.secret}")
    private String secret;

    public static boolean login(User user){
        String password = user.getPassword();
        user = auth.userMapper.getUserByUsername(user.getUsername());
        if (user != null){
            return user.getPassword().equals(password);
        }
        return false;
    }



    public static String tokenGen(int id, boolean is_admin){
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("is_admin", is_admin);
        String sub = JSONUtils.toJSONString(payload);
        System.out.println("sub = " + sub);

        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setSubject(sub)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 1000L * auth.EXPIRE_SEC))
                .signWith(SignatureAlgorithm.HS512, auth.secret)
                .compact();
    }

    public static Claims tokenParse(String token){
        return Jwts.parser()
                .setSigningKey(auth.secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Claims getClaimFromRequest(HttpServletRequest request){
        String token = request.getHeader("token");
        if (token != null && !token.equals("")) {
            return tokenParse(token);
        } else {
            return null;
        }
    }

    public static TokenSubject getSubFromClaim(Claims claims) throws JsonProcessingException {
        return auth.objectMapper.readValue(claims.getSubject(), TokenSubject.class);
    }
}
