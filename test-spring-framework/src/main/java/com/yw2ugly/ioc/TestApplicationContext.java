package com.yw2ugly.ioc;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestApplicationContext {

    public static void main(String[] args) {
        testClassPathXmlApplicationContext();
        testFileSystemXmlApplicationContext();
        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();

    }

    private static void testAnnotationConfigApplicationContext() {
        // TODO: 2023/8/5  
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();


    }

    private static void testFileSystemXmlApplicationContext() {
        // TODO: 2023/8/5  
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext();

    }

    private static void testClassPathXmlApplicationContext() {
        // TODO: 2023/8/5
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
    }

    private static void testAnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext applicationContext = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);





    }


    @Configuration
    static class WebConfig {

        //servlet web server (Tomcat,Jboss..)
        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return  new TomcatServletWebServerFactory();
        }

        //config a dispatcher servlet(front dispatcher)
        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        //register servlet to web server
        @Bean
        public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet){
            return new DispatcherServletRegistrationBean(dispatcherServlet,"/");
        }

        // controller
        @Bean("/hello")
        public Controller controller(){
            return new Controller() {
                @Override
                public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
                    response.getWriter().write("hello world !!!\n");
                    return null;
                }
            };
        }

    }


}


