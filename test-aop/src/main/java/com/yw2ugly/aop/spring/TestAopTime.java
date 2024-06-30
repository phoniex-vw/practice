package com.yw2ugly.aop.spring;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

//aop 的时机
// 创建 -> （提前aop,循环依赖时) -> 依赖注入 -> 初始化 -> (正常aop)
public class TestAopTime {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean(Bean1.class);
        applicationContext.registerBean(Bean2.class);
        applicationContext.registerBean(Bean3.class);
        applicationContext.registerBean(Bean4.class);
        applicationContext.registerBean(Aspect1.class);
        applicationContext.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        applicationContext.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        applicationContext.registerBean(CommonAnnotationBeanPostProcessor.class);

        applicationContext.addBeanFactoryPostProcessor(new ConfigurationClassPostProcessor());

        applicationContext.refresh();
        applicationContext.close();

    }

    @Slf4j
    @Aspect
    static class Aspect1{
        @Before("execution(* foo())")
        public void before(){
            log.info("Aspect before ...");
        }
    }

    @Slf4j
    static class Bean1 {
        int count;

        public Bean1() {
            log.info("{} Bean1 created ...", count++);
        }

        @PostConstruct
        public void init(){
            log.info("{} Bean1 initialized ...", count++);
        }

        public void foo(){}
    }

    @Slf4j
    static class Bean2 {
        int count;
        Bean1 bean1;

        public Bean2() {
            log.info("{} Bean2 created ...", count++);
        }

        @Autowired
        public void setBean1(Bean1 bean1){
            log.info("{} Bean2 Autowired, Bean1: {} ...", count++, bean1.getClass());
            this.bean1 = bean1;
        }

        @PostConstruct
        public void init(){
            log.info("{} Bean2 initialized ...", count++);
        }
    }

    @Slf4j
    static class Bean3 {
        int count;
        Bean4 bean4;

        public Bean3() {
            log.info("{} Bean3 created ...", count++);
        }

        @Autowired
        public void setBean4(Bean4 bean4){
            log.info("{} Bean3 Autowired, Bean4: {} ...", count++, bean4.getClass());
            this.bean4 = bean4;
        }
        @PostConstruct
        public void init() {
            log.info("{} Bean3 initialized ...", count++);
        }

        public void foo() {
        }
    }

    @Slf4j
    static class Bean4 {
        int count;
        Bean3 bean3;

        public Bean4() {
            log.info("{} Bean4 created ...", count++);
        }

        @Autowired
        public void setBean3(Bean3 bean3){
            log.info("{} Bean4 Autowired, Bean3: {} ...", count++, bean3.getClass());
            this.bean3 = bean3;
        }

        @PostConstruct
        public void init(){
            log.info("{} Bean4 initialized ...", count++);
        }
    }
}
