package com.curude.productapi.config.interceptor;

import com.curude.productapi.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtService jwtService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(isOptions(request)){
            return true;
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAuthorization(authorization);

        return true;
    }

    private boolean isOptions(HttpServletRequest request){
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }

}
