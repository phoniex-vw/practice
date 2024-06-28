package com.yw2ugly.ioc.scope;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SpringBootApplication
public class TestScope {
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestScope.class);

    }

    @RestController
    static class HomeController{

        @Autowired
        @Lazy
        private BeanForRequest beanForRequest;

        @Autowired
        @Lazy
        private BeanForSession beanForSession;

        @Autowired
        @Lazy
        private BeanForApplication beanForApplication;


        @GetMapping(value = "/test",produces = "text/html")
        public String test(HttpServletRequest request, HttpSession session){
            ServletContext sc = request.getServletContext();

            String sb = "<ul>" +
                    "<li> request scope : " + beanForRequest + "</li>" +
                    "<li> session scope : " + beanForSession + "</li>" +
                    "<li> application scope : " + beanForApplication + "</li>" +
                    "</ul>";

            return sb;

        }
    }




    @Component
    @Scope("request")
    @Slf4j
    static class BeanForRequest{

        @PreDestroy
        public void destroy(){
            log.info("BeanForRequest destroy ..");
        }
    }

    @Component
    @Scope("session")
    static class BeanForSession{}

    @Component
    @Scope("application")
    static class BeanForApplication{}

}
