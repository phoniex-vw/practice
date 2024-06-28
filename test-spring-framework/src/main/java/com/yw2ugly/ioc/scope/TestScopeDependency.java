package com.yw2ugly.ioc.scope;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

// single bean  depends on Scope bean
@Slf4j
public class TestScopeDependency {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(Config.class);

        A a = configApplicationContext.getBean(A.class);

        log.debug("{}",a.getF1());
        log.debug("{}",a.getF1());

        log.debug("{}",a.getF2());
        log.debug("{}",a.getF2());

        log.debug("{}",a.getF3());
        log.debug("{}",a.getF3());

        log.debug("{}",a.getF4());
        log.debug("{}",a.getF4());


    }

    @Configuration
    @Slf4j
    @Import({A.class,F1.class,F2.class,F3.class,F4.class})
    static class Config {

    }
    @Data
    @Slf4j
    static class A  implements ApplicationContextAware {

        private ApplicationContext applicationContext;
        @Autowired
        @Lazy
        private F1 f1;
        @Autowired
        private F2 f2;
        @Autowired
        private ObjectFactory<F3> f3;

        private F4 f4;

        public F3 getF3(){
            return  this.f3.getObject();
        }

        public F4 getF4(){
            return this.applicationContext.getBean(F4.class);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

    @Component
    @Scope("prototype")
    static class F1{}
    @Component
    @Scope(value = "prototype",proxyMode = ScopedProxyMode.TARGET_CLASS)
    static class F2{}
    @Component
    @Scope("prototype")
    static class F3{}
    @Component
    @Scope("prototype")
    static class F4{}


}
