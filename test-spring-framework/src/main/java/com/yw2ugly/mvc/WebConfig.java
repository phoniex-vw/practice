package com.yw2ugly.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ComponentScan(basePackages = {"com.yw2ugly.mvc"})
public class WebConfig {

    @Bean
    RequestMappingHandlerMapping requestMappingHandlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
        return new MyRequestMappingHandlerAdapter();
    }


    static class MyRequestMappingHandlerAdapter extends  RequestMappingHandlerAdapter {

        @Override
        public ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
            return super.invokeHandlerMethod(request, response, handlerMethod);
        }
    }

}
