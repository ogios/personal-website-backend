package com.example.springtest_backend.interceptor;

import com.example.springtest_backend.utils.Auth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("TokenIntercept");
        try{
            Auth.getClaimFromRequest(request);
            return true;
        } catch (Exception e){
//            e.printStackTrace();
            response.setStatus(401);
            return false;
        }
    }
}
