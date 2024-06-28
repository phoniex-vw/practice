package com.yw2ugly.mvc;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExceptionHandlerExceptionResolver {

    public static void main(String[] args) throws NoSuchMethodException, UnsupportedEncodingException {
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = getExceptionHandlerExceptionResolver();


        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        mockHttpServletResponse.setContentType("application/json");

        HandlerMethod foo = new HandlerMethod(new Controller1(), Controller1.class.getMethod("foo"));

        Exception arithmeticException = new ArithmeticException("被零除");
        exceptionHandlerExceptionResolver.resolveException(mockHttpServletRequest, mockHttpServletResponse, foo, arithmeticException);

        System.err.println(new String(mockHttpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8));

    }

    private static ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionHandlerExceptionResolver.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
        exceptionHandlerExceptionResolver.afterPropertiesSet();

        List<HttpMessageConverter<?>> messageConverters = exceptionHandlerExceptionResolver.getMessageConverters();
        HandlerMethodReturnValueHandlerComposite returnValueHandlers = exceptionHandlerExceptionResolver.getReturnValueHandlers();
        HandlerMethodArgumentResolverComposite argumentResolvers = exceptionHandlerExceptionResolver.getArgumentResolvers();
        return exceptionHandlerExceptionResolver;
    }

    static class Controller1{
        public void foo(){

        }

        @ResponseBody
        @ExceptionHandler
        public Map<String,String> handException(ArithmeticException e){
            Map<String, String> map = new HashMap<>();
            map.put("error",e.getMessage());
            return map ;
        }
    }
}
