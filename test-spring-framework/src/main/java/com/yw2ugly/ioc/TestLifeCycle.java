package com.yw2ugly.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class TestLifeCycle {

    static int count = 1;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(Config.class);
        LifeCycleBean lifeCycleBean = configApplicationContext.getBean("lifeCycleBean", LifeCycleBean.class);
        configApplicationContext.close();

    }

    @Configuration
    @Import({CustomBeanPostProcessor.class})
    static class Config{
        @Bean(initMethod = "initMethod",destroyMethod = "destroyMethod")
        public LifeCycleBean lifeCycleBean(){
            return  new LifeCycleBean();
        }
    }

    @Slf4j
    static class LifeCycleBean implements InitializingBean , DisposableBean {

        public LifeCycleBean(){
          log.info("{} LifeCycleBean initialization",count++);
        }


        @Autowired
        public void autoWire(@Value("${java.dir}") String home){
            log.info("{} LifeCycleBean @autowire ",count++);
        }


        @PostConstruct
        public void postConstruct(){
            log.info("{} LifeCycleBean @PostConstruct",count++);
        }


        public void initMethod(){
            log.info("{} LifeCycleBean init method",count++);
        }
        public void destroyMethod(){log.info("{} LifeCycleBean destroy method",count++);};


        @PreDestroy
        public void preDestroy(){
            log.info("{} LifeCycleBean @PreDestroy",count++);
        }


        public void destroy(){
            log.info("{} LifeCycleBean Disposable destroy method",count++);
        }


        @Override
        public void afterPropertiesSet() throws Exception {
            log.info("{} LifeCycleBean InitializationBean afterPropertiesSet method",count++);
        }

    }

    @Slf4j
    @Component
    static class CustomBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor{

        @Override
        public void postProcessBeforeDestruction(Object o, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")){
                log.info("{} postProcessBeforeDestruction ",count++);
            }

        }

        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")){
                log.info("{} postProcessBeforeInstantiation ",count++);
            }
            return  null;
        }

        @Override
        public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")){
                log.info("{} postProcessAfterInstantiation ",count++);
            }
            return  true;
        }

        @Override
        public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")){
                log.info("{} postProcessProperties ",count++);
            }
            return  pvs;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")){
                log.info("{} postProcessBeforeInitialization ",count++);
            }
            return  bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (beanName.equals("lifeCycleBean")){
                log.info("{} postProcessAfterInitialization ",count++);
            }
            return  bean;
        }
    }

}
