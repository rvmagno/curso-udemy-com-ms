package com.curude.productapi.config.interceptor;

import com.curude.productapi.config.exception.AuthenticationException;
import com.curude.productapi.config.exception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeighClientAuthInterceptor implements RequestInterceptor {

    public static final String AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var currentRequest = getCurrentRequest();

        requestTemplate.header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION));
    }

    private HttpServletRequest getCurrentRequest(){
        try{
            return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e){
            e.printStackTrace();
            throw new ValidationException("the current request could not be processed.");
        }
    }

}
