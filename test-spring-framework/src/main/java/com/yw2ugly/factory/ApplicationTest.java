package com.yw2ugly.factory;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

public class ApplicationTest {

    public static void main(String[] args) {
        testAnnotationConfigWebServerApplicationContext();
        //testClassPathXmlApplicationContext();
        //testAnnotationConfigApplicationContext();
    }

    private static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // TODO: 2024/6/29
    }

    private static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        // TODO: 2024/6/29
    }

    /**
     * web环境下使用的容器
     */
    private static void testAnnotationConfigWebServerApplicationContext() {
        // 启动一个tomcat server，并关联DispatcherServlet
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebMvcConfig.class);

    }

    @Configuration
    public static class WebMvcConfig {

        @Bean
        public ServletWebServerFactory webServerFactory(){
            return  new TomcatServletWebServerFactory("",9999);
        }

        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean servletRegistrationBean(){
            return new DispatcherServletRegistrationBean(dispatcherServlet(),"/hello");
        }

        @Bean("/hello")
        public Controller helloController(){
            return (request, response) -> {
                response.getWriter().println("hello tomcat");
                return null;
            };
        }

    }
}
