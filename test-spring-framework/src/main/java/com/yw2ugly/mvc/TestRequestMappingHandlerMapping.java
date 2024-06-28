package com.yw2ugly.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Slf4j
public class TestRequestMappingHandlerMapping {

    public static void main(String[] args) throws Exception {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebConfig.class);
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        System.err.println(applicationContext.getBean(HelloController.class));

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        handlerMethods.forEach((requestMappingInfo,handlerMethod) -> {
            log.info("requestMappingInfo >>>> {} , handler method  >>>> {}",requestMappingInfo,handlerMethod);
        });

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET", "/t1");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(mockHttpServletRequest);
        assert handlerExecutionChain != null;
        HandlerMethod handler = (HandlerMethod) handlerExecutionChain.getHandler();


        WebConfig.MyRequestMappingHandlerAdapter requestMappingHandlerAdapter = (WebConfig.MyRequestMappingHandlerAdapter) applicationContext.getBean(RequestMappingHandlerAdapter.class);
        //获取参数名
        requestMappingHandlerAdapter.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        //内容协商管理器
        requestMappingHandlerAdapter.setContentNegotiationManager(null);

        ModelAndView modelAndView = requestMappingHandlerAdapter.invokeHandlerMethod(mockHttpServletRequest, mockHttpServletResponse, handler);

        String contentAsString = mockHttpServletResponse.getContentAsString();
        log.info("response >>>> {} ",contentAsString);


    }

}
